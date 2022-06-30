package com.github.dankinsoid.appcodeassets.services.ui

import com.github.dankinsoid.appcodeassets.models.*
import com.github.dankinsoid.appcodeassets.services.ui.extensions.addCompressionSection
import com.github.dankinsoid.appcodeassets.services.ui.extensions.addDirectionSection
import com.github.dankinsoid.appcodeassets.services.ui.extensions.addResizingSection
import com.github.dankinsoid.appcodeassets.services.ui.extensions.addScalesSection
import com.google.gson.GsonBuilder
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.ui.WrapLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.*
import javax.swing.border.EmptyBorder

class ImageSetComponent(val file: VirtualFile): Box(BoxLayout.PAGE_AXIS) {

    private var gamutComboBox: ComboBox<String>? = null
    private var renderAsComboBox: ComboBox<String>? = null
    private var compressionComboBox: ComboBox<String>? = null
    private var scalesComboBox: ComboBox<String>? = null
    private var directionComboBox: ComboBox<String>? = null

    private var devicePanels: MutableMap<Idiom, JPanel> = mutableMapOf()
    private var deviceSections: MutableMap<Idiom, Section<JPanel>> = mutableMapOf()
    private var deviceCheckboxes: MutableMap<Idiom, JCheckBox> = mutableMapOf()
    private var fileBoxes: MutableMap<ImageSetKey, ComboBox<String>> = mutableMapOf()
    private var fileSections: MutableMap<ImageSetKey, Section<ComboBox<String>>> = mutableMapOf()
    private var appearancesComboBox: ComboBox<String>? = null
    private var highContrastCheckBox: JCheckBox? = null
    private var resizingCheckBox: JCheckBox? = null

    private val spacing = 10
    private var canUpdate = false

    init {
//        val panel = JPanel(GridLayout(3, 3)).apply {
            renderAsComboBox = addRenderAsSection { updateSet() }
            add(createRigidArea(Dimension(0, spacing)))

            compressionComboBox = addCompressionSection { updateSet() }
            add(createRigidArea(Dimension(0, spacing)))

            resizingCheckBox = addResizingSection { updateSet() }
            add(createRigidArea(Dimension(0, spacing)))

            val appearancesPair = addAppearencesSection { updateSet() }
            appearancesComboBox = appearancesPair.first
            highContrastCheckBox = appearancesPair.second
            add(createRigidArea(Dimension(0, spacing)))

            scalesComboBox = addScalesSection { updateSet() }
            add(createRigidArea(Dimension(0, spacing)))

            gamutComboBox = addGamutSection { updateSet() }
            add(createRigidArea(Dimension(0, spacing)))

            directionComboBox = addDirectionSection { updateSet() }
//        }
//        addSection("Settings") { panel }

        for (device in Idiom.setValues()) {
            add(createRigidArea(Dimension(0, spacing)))
            val checkbox = JCheckBox(device.title)
            checkbox.addItemListener { updateSet() }
            deviceCheckboxes[device] = checkbox
            add(
                Section<JPanel>(checkbox).apply {
                    deviceSections[device] = this
                }
            )
        }
        add(Filler(Dimension(0, 0), Dimension(0, 0), Dimension(0, Int.MAX_VALUE)))
        border = EmptyBorder(spacing, spacing, spacing, spacing)

        setupUI()
        canUpdate = true
    }

    private fun setupUI() {
        val imageSet = this.imageSet

        /// MARK: - Devices
        val idioms = imageSet.images?.map { it.idiom } ?: listOf()
        deviceCheckboxes.forEach { (idiom, checkbox) ->
            checkbox.isSelected = idioms.contains(idiom)
        }

        renderAsComboBox?.selectedIndex = if (imageSet.properties?.templateRenderingIntent == null) 0 else
            TemplateRenderingIntent.values().indexOf(imageSet.properties?.templateRenderingIntent ?: TemplateRenderingIntent.original) + 1

        compressionComboBox?.selectedIndex = if (imageSet.properties?.compressionType == null) 0 else
            CompressionType.values().indexOf(imageSet.properties?.compressionType ?: CompressionType.automatic) + 1

        resizingCheckBox?.isSelected = imageSet.properties?.preservesVectorRepresentation == true

        /// MARK: - Appearances
        val appearances = Appearances.fromArray(imageSet.images?.flatMap { it.appearances?.map { it.value } ?: listOf() }?.toTypedArray() ?: arrayOf())
        appearancesComboBox?.selectedIndex = Appearances.values().indexOf(appearances)

        highContrastCheckBox?.isSelected = imageSet.images?.flatMap { it.appearances?.map { it.appearance } ?: listOf() }?.contains(AppearanceType.contrast) ?: false

        val scales = Scales.fromArray(imageSet.images?.map { it.scale }?.toTypedArray() ?: arrayOf())
        scalesComboBox?.selectedIndex = Scales.values().indexOf(scales)

        /// MARK: - Gamut
        val gamuts = Gamuts.fromArray(imageSet.images?.mapNotNull { it.gamut }?.toTypedArray() ?: arrayOf())
        gamutComboBox?.selectedIndex = Gamuts.values().indexOf(gamuts)

        val directions = LanguageDirections.fromArray(imageSet.images?.mapNotNull { it.languageDirection }?.toTypedArray() ?: arrayOf())
        directionComboBox?.selectedIndex = LanguageDirections.values().indexOf(directions)

        updateVisibles()
    }

    private fun updateSet() {
        if (!canUpdate) return
        val imageSet = imageSet
        val images: MutableList<Images> = mutableListOf()
        for (device in Idiom.setValues().filter { deviceCheckboxes[it]?.isSelected ?: false }) {
            for (appearance in selectedAppearances) {
                for (gamut in selectesGamuts) {
                    for (contrast in selectedConstrants) {
                        for (scale in selectedScales) {
                            for (direction in selectedDirections) {
                                val appearances = Appearance.list(contrast, appearance)
                                val key: ImageSetKey = Pair(Pair(device, appearance), Pair(Pair(contrast, scale), Pair(gamut, direction)))
                                val file = fileBoxes[key]?.selectedItem as? String ?: ""

                                val image = Images(
                                    filename = file,
                                    idiom = device,
                                    appearances = appearances.ifEmpty { null },
                                    scale = scale?.toScale(),
                                    gamut = gamut,
                                    languageDirection = direction
                                )
                                images.add(image)
                            }
                        }
                    }
                }
            }
        }
        imageSet.images = images
        val properties = ImageSetProperties(
            templateRenderingIntent = selectedTemplate,
            compressionType = selectedCompression,
            preservesVectorRepresentation = resizingCheckBox?.isSelected ?: false
        )
        if (properties.isEmpty) {
            imageSet.properties = null
        } else {
            imageSet.properties = properties
        }
        this.imageSet = imageSet
        canUpdate = false
        updateVisibles()
        canUpdate = true
    }

    private fun updateVisibles() {
        val idioms = imageSet.images?.map { it.idiom } ?: listOf()

        Idiom.setValues().forEach {
            devicePanel(it, !idioms.contains(it))
        }

        val images = this.images

        fileSections.forEach { (key, section) ->
            section.isVisible = images[key] != null
        }

        fileBoxes.forEach { (key, box) ->
            box.selectedItem = images[key]?.filename
        }
    }

    private val images: Map<ImageSetKey, Images>
        get() = imageSet.images?.associate { image ->
            val appearance = image.appearances?.firstOrNull { it.appearance == AppearanceType.luminosity }
            val highContrast = image.appearances?.firstOrNull { it.appearance == AppearanceType.contrast }?.value == AppearanceValue.high
            Pair(
                Pair(image.idiom, appearance?.value),
                Pair(
                    Pair(highContrast, image.scale?.substringBefore("x")?.toInt()),
                    Pair(image.gamut, image.languageDirection)
                )
            ) to image
        } ?: mapOf()

    private fun files(): Array<String> {
        val files = file.parent?.children?.filter { it.extension != "json" } ?: return arrayOf()
        return arrayOf("") + files.map { it.name }.toTypedArray()
    }

    private var imageSet: ImageSet
        get() = ImageSet.get(file)
        set(value) = try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val json = gson.toJson(value)
            val runned = ApplicationManager.getApplication()?.runWriteAction {
                try {
                    file.setBinaryContent(json.toByteArray(Charsets.UTF_8))
                } catch (_: Throwable) {
                }
            }
        } catch (_: Throwable) {
        }

    private fun devicePanel(device: Idiom, hide: Boolean): JPanel? {
        val panel = devicePanels[device]
        if ((panel == null) == hide) {
            return panel
        }
        if (hide) {
            panel?.parent?.remove(panel)
            devicePanels.remove(device)
            return null
        } else {
            setContent(device)
            return devicePanels[device]
        }
    }

    private fun setContent(device: Idiom) {
        val section = deviceSections[device] ?: return
        section.setContent {
            JPanel(WrapLayout(FlowLayout.LEADING, spacing, spacing)).apply {
                alignmentX = Component.LEFT_ALIGNMENT
                for (appearance in allAppearances) {
                    for (highContrast in listOf(false, true)) {
                        for (gamut in listOf(null) + Gamut.values()) {
                            for (scale in Scales.scales) {
                                for (direction in LanguageDirections.directions) {
                                    val title = listOf(
                                        "scale: ${scale?.toScale() ?: "Any"}",
                                        "gamut: ${gamut?.title ?: "Any"}",
                                        "direction: ${direction?.title ?: "Any"}",
                                        "appearance: ${appearance?.title ?: "Any"}",
                                        "contrast: ${if (highContrast) "High" else "Normal"}"
                                    ).joinToString("\n")

                                    val key: ImageSetKey = Pair(Pair(device, appearance), Pair(Pair(highContrast, scale), Pair(gamut, direction)))
                                    add(
                                        Section(title) {
                                            ComboBox<String>().apply {
                                                model = DefaultComboBoxModel(files())
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
    }

    private val allAppearances: List<AppearanceValue?>
        get() = listOf(null, AppearanceValue.light, AppearanceValue.dark)

    private val selectedAppearances: List<AppearanceValue?>
        get() = Appearances.values()[appearancesComboBox?.selectedIndex ?: 0].appearances

    private val selectedConstrants: List<Boolean>
        get() = if (highContrastCheckBox?.isSelected == true) { listOf(false, true) } else { listOf(false) }

    private val selectesGamuts: List<Gamut?>
        get() = Gamuts.values()[gamutComboBox?.selectedIndex ?: 0].gamuts

    private val selectedScales: List<Int?>
        get() = Scales.values()[scalesComboBox?.selectedIndex ?: 0].scales

    private val selectedDirections: List<LanguageDirection?>
        get() = LanguageDirections.values()[directionComboBox?.selectedIndex ?: 0].directions

    private val selectedTemplate: TemplateRenderingIntent?
        get() = if (renderAsComboBox?.selectedIndex == 0) null else TemplateRenderingIntent.values()[renderAsComboBox?.selectedIndex?.minus(1) ?: 0]

    private val selectedCompression: CompressionType?
        get() = if (compressionComboBox?.selectedIndex == 0) null else CompressionType.values()[compressionComboBox?.selectedIndex?.minus(1) ?: 0]
}

typealias ImageSetKey = Pair<Pair<Idiom, AppearanceValue?>, Pair<Pair<Boolean?, Int?>, Pair<Gamut?, LanguageDirection?>>>
