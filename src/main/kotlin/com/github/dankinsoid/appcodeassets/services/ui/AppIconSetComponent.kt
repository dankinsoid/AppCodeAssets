package com.github.dankinsoid.appcodeassets.services.ui

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
        addSection("Gamut") {
            Box(BoxLayout.LINE_AXIS).apply {
                add(
                    ComboBox<String>().apply {
                        model = DefaultComboBoxModel(Gamuts.values().map { it.title }.toTypedArray())
                        gamutComboBox = this
                        addItemListener { updateSet() }
                    }
                )
                add(Filler(Dimension(0, 0), Dimension(0, 0), Dimension(Int.MAX_VALUE, 0)))
            }
        }
        for ((device, templates) in AppIcon.sets) {
            add(createRigidArea(Dimension(0, spacing)))
            val checkbox = JCheckBox(device.title)
            checkbox.addChangeListener { updateSet() }
            deviceCheckboxs[device] = checkbox
            add(
                Section(checkbox) {
                    JPanel(WrapLayout(FlowLayout.LEADING)).apply {
                        alignmentX = Component.LEFT_ALIGNMENT
                        for (template in templates) {
                            for (size in template.sizes) {
                                for (gamut in template.gamuts(gamutComboBox?.selectedIndex != 0)) {
                                    for (scale in template.scales) {
                                        for (direction in template.directions()) {
                                            val title = listOf(
                                                "size: ${size.toSize()}",
                                                "gamut: ${gamut?.title ?: "Any"}",
                                                "scale: ${scale?.toScale() ?: "Any"}",
                                                "direction: ${direction?.title ?: "Any"}"
                                            ).joinToString("\n")
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

    }

    private fun updateVisibles() {
        val idioms = appIconSet.images?.map { it.idiom } ?: listOf()

        devicePanels.forEach {
            it.value.isVisible = idioms.contains(it.key)
        }

        val filenames = this.filenames

        fileSections.forEach { (key, section) ->
            section.isVisible = filenames.containsKey(key)
        }

        fileBoxes.forEach { (key, box) ->
            box.selectedItem = filenames[key]
        }
    }

    private val filenames: Map<AppIconSetKey, String?>
        get() = appIconSet.images?.associate { image ->
            Pair(
                Pair(image.idiom, image.size?.substringBefore("x")?.toDouble()),
                Pair(
                    Pair(image.displayGamut, image.scale?.substringBefore("x")?.toInt()),
                    image.languageDirection
                )
            ) to image.filename
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
        return files.map { it.name }.toTypedArray()
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
}

typealias AppIconSetKey = Pair<Pair<Idiom, Double?>, Pair<Pair<Gamut?, Int?>, LanguageDirection?>>
