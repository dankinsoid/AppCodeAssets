package com.github.dankinsoid.appcodeassets.services.ui

import com.github.dankinsoid.appcodeassets.extensions.nullIfEmpty
import com.github.dankinsoid.appcodeassets.extensions.pngSize
import com.github.dankinsoid.appcodeassets.models.*
import com.google.gson.GsonBuilder
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.ui.WrapLayout
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.*
import javax.swing.border.EmptyBorder

class AppIconSetComponent(val file: VirtualFile): Box(BoxLayout.PAGE_AXIS) {

    private var gamutComboBox: ComboBox<String>? = null
    private var devicePanels: MutableMap<Idiom, JPanel> = mutableMapOf()
    private var deviceCheckboxs: MutableMap<Idiom, JCheckBox> = mutableMapOf()
    private var fileBoxes: MutableMap<AppIconSetKey, ComboBox<String>> = mutableMapOf()
    private var fileSections: MutableMap<AppIconSetKey, Section<ComboBox<String>>> = mutableMapOf()

    private val spacing = 10
    private var canUpdate = false

    init {
        gamutComboBox = addGamutSection { updateSet() }
        for ((device, templates) in AppIcon.sets) {
            add(createRigidArea(Dimension(0, spacing)))
            val checkbox = JCheckBox(device.title)
            checkbox.addItemListener { updateSet() }
            deviceCheckboxs[device] = checkbox
            add(
                Section(checkbox) {
                    JPanel(WrapLayout(FlowLayout.LEADING, spacing, spacing)).apply {
                        alignmentX = Component.LEFT_ALIGNMENT
                        for (template in templates) {
                            for (size in template.sizes) {
                                for (gamut in listOf(null) + Gamut.values()) {
                                    for (scale in template.scales) {
                                        for (direction in template.directions()) {
                                            var title = listOf(
                                                "${size?.toSize() ?: "Any"} pt",
                                                "${((size ?: 0.0) * (scale ?: 1).toDouble()).toSize()} px",
                                                "scale: ${scale?.toScale() ?: "Any"}",
                                                "gamut: ${gamut?.title ?: "Any"}",
                                                "direction: ${direction?.title ?: "Any"}"
                                            ).joinToString("\n")
                                            if (template.role != null || template.subtype != null) {
                                                title += "\nrole: ${template.role?.title ?: "Null"}"
                                                title += "\nsubtype: ${template.subtype ?: "Null"}"
                                            }
                                            val key: AppIconSetKey = Pair(Pair(device, size), Pair(Pair(gamut, scale), direction))
                                            add(
                                                Section(title) {
                                                    ComboBox<String>().apply {
                                                        model = DefaultComboBoxModel(files(key))
                                                        fileBoxes[key] = this
                                                        addItemListener { updateSet() }
                                                    }
                                                }.apply {
                                                    alignmentY = BOTTOM_ALIGNMENT
                                                    fileSections[key] = this
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }.apply {
                        devicePanels[device] = this
                    }
                }
            )
        }
        add(Filler(Dimension(0, 0), Dimension(0, 0), Dimension(0, Int.MAX_VALUE)))
        border = EmptyBorder(spacing, spacing, spacing, spacing)

        setupUI()
        canUpdate = true
    }

    private fun setupUI() {
        val appIconSet = this.appIconSet

        /// MARK: - Devices
        val idioms = appIconSet.images?.map { it.idiom } ?: listOf()
        deviceCheckboxs.forEach { (idiom, checkbox) ->
            checkbox.isSelected = idioms.contains(idiom)
        }

        /// MARK: - Gamut
        val gamuts = Gamuts.fromArray(appIconSet.images?.mapNotNull { it.displayGamut }?.toTypedArray() ?: arrayOf())
        gamutComboBox?.selectedIndex = Gamuts.values().indexOf(gamuts)

        updateVisibles()
    }

    private fun updateSet() {
        if (!canUpdate) return
        val appIconSet = appIconSet
        val images: MutableList<AppIcon> = mutableListOf()
        for (device in AppIcon.sets.keys.filter { deviceCheckboxs[it]?.isSelected ?: false }) {
            val templates = AppIcon.sets[device] ?: continue
            for (template in templates) {
                for (gamut in template.gamuts(gamutComboBox?.selectedIndex != 0)) {
                    for (size in template.sizes) {
                        for (scale in template.scales) {
                            for (direction in template.directions()) {
                                val key: AppIconSetKey =
                                    Pair(Pair(device, size), Pair(Pair(gamut, scale), direction))
                                val file = fileBoxes[key]?.selectedItem as? String ?: ""
                                val image = AppIcon(
                                    idiom = device,
                                    size = size?.toSize(),
                                    scale = scale?.toScale(),
                                    languageDirection = direction,
                                    filename = file.nullIfEmpty(),
                                    subtype = template.subtype,
                                    displayGamut = gamut,
                                    role = template.role
                                )
                                images.add(image)
                            }
                        }
                    }
                }
            }
        }
        appIconSet.images = images
        this.appIconSet = appIconSet
        canUpdate = false
        updateVisibles()
        canUpdate = true
    }

    private fun updateVisibles() {
        val idioms = appIconSet.images?.map { it.idiom } ?: listOf()

        devicePanels.forEach {
            it.value.isVisible = idioms.contains(it.key)
        }

        val images = this.images

        fileSections.forEach { (key, section) ->
            section.isVisible = images[key] != null
        }

        fileBoxes.forEach { (key, box) ->
            box.selectedItem = images[key]?.filename
        }
    }

    private val images: Map<AppIconSetKey, AppIcon>
        get() = appIconSet.images?.associate { image ->
            Pair(
                Pair(image.idiom, image.size?.substringBefore("x")?.toDouble()),
                Pair(
                    Pair(image.displayGamut, image.scale?.substringBefore("x")?.toInt()),
                    image.languageDirection
                )
            ) to image
        } ?: mapOf()

    private fun files(key: AppIconSetKey): Array<String> {
        var files = file.parent?.children?.filter { it.name.endsWith(".png") } ?: return arrayOf()
        val size = key.first.second
        val scale = key.second.first.second?.toDouble()
        if (scale != null && size != null) {
            val width = (size * scale).toInt()
            files = files.filter {
                val imageSize = it.pngSize() ?: return@filter false
                imageSize.width == width && imageSize.height == width
            }
        }
        return arrayOf("") + files.map { it.name }.toTypedArray()
    }

    var appIconSet: AppIconSet
        get() = AppIconSet.get(file)
        set(value) = try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val json = gson.toJson(value)
            val runned = ApplicationManager.getApplication()?.runWriteAction {
                try {
                    file.setBinaryContent(json.toByteArray(Charsets.UTF_8))
                } catch (e: Throwable) {
                }
            }
        } catch (_: Throwable) {
        }

    private val selectesGamuts: List<Gamut?>
        get() = Gamuts.values()[gamutComboBox?.selectedIndex ?: 0].gamuts
}

typealias AppIconSetKey = Pair<Pair<Idiom, Double?>, Pair<Pair<Gamut?, Int?>, LanguageDirection?>>
