package com.github.dankinsoid.appcodeassets.services.ui

import com.github.dankinsoid.appcodeassets.models.*
import com.google.gson.GsonBuilder
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.ColorPanel
import com.intellij.util.ui.WrapLayout
import java.awt.*
import javax.swing.*
import javax.swing.border.EmptyBorder

class ColorAssetComponent(val file: VirtualFile): Box(BoxLayout.PAGE_AXIS) {

    private var deviceCheckboxs: MutableMap<Idiom, JCheckBox> = mutableMapOf()
    private var devicePanels: MutableMap<Idiom, JPanel> = mutableMapOf()
    private var gamutComboBox: ComboBox<String>? = null
    private var appearancesComboBox: ComboBox<String>? = null
    private var highContrastCheckBox: JCheckBox? = null
    private var colorBoxes: MutableMap<ColorAssetKey, Section<Box>> = mutableMapOf()
    private var colorSpacesComboBox: MutableMap<ColorAssetKey, ComboBox<String>> = mutableMapOf()
    private var colorPanels: MutableMap<ColorAssetKey, ColorPanel> = mutableMapOf()

    private val spacing = 10
    private var canUpdate = false

    init {
        val appearancesPair = addAppearencesSection { updateColorSet() }
        appearancesComboBox = appearancesPair.first
        highContrastCheckBox = appearancesPair.second
        add(createRigidArea(Dimension(0, spacing)))
        gamutComboBox = addGamutSection { updateColorSet() }

        for (device in Idiom.setValues()) {
            add(createRigidArea(Dimension(0, spacing)))
            val checkbox = JCheckBox(device.title)
            checkbox.addItemListener { updateColorSet() }
            deviceCheckboxs[device] = checkbox
            add(
                Section(checkbox) {
                    JPanel(WrapLayout(FlowLayout.LEADING, spacing, spacing)).apply {
                        alignmentX = Component.LEFT_ALIGNMENT
                        for (appearance in allAppearances) {
                            for (highContrast in listOf(false, true)) {
                                for (gamut in allGamuts) {
                                    add(
                                        Section("${appearance?.title ?: "Any"} AppearanceType\n${if (highContrast) "High" else "Normal"} Contrast\n${gamut?.title ?: "Any"} Gamut") {
                                            Box(BoxLayout.PAGE_AXIS).apply {
                                                add(
                                                    ComboBox<String>().apply {
                                                        model = DefaultComboBoxModel(ColorSpace.values().map { it.title }.toTypedArray())
                                                        colorSpacesComboBox[Pair(Pair(device, appearance), Pair(highContrast, gamut))] = this
                                                        addItemListener { updateColorSet() }
                                                    }
                                                )
                                                add(
                                                    ColorPanel().apply {
                                                        setSupportTransparency(true)
                                                        colorPanels[Pair(Pair(device, appearance), Pair(highContrast, gamut))] = this
                                                        addActionListener { updateColorSet() }
                                                    }
                                                )
                                            }
                                        }.apply {
                                            alignmentY = BOTTOM_ALIGNMENT
                                            colorBoxes[Pair(Pair(device, appearance), Pair(highContrast, gamut))] = this
                                        }
                                    )
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

    var colorSet: ColorSet
        get() = ColorSet.get(file)
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

    private fun setupUI() {
        val colorSet = this.colorSet

        /// MARK: - Devices
        val idioms = colorSet.colors?.map { it.idiom ?: Idiom.universal } ?: listOf()
        deviceCheckboxs.forEach { (idiom, checkbox) ->
            checkbox.isSelected = idioms.contains(idiom)
        }

        /// MARK: - Gamut
        val gamuts = Gamuts.fromArray(colorSet.colors?.mapNotNull { it.gamut }?.toTypedArray() ?: arrayOf())
        gamutComboBox?.selectedIndex = Gamuts.values().indexOf(gamuts)

        /// MARK: - Appearances
        val appearances = Appearances.fromArray(colorSet.colors?.flatMap { it.appearances?.map { it.value } ?: listOf() }?.toTypedArray() ?: arrayOf())
        appearancesComboBox?.selectedIndex = Appearances.values().indexOf(appearances)

        highContrastCheckBox?.isSelected = colorSet.colors?.flatMap { it.appearances?.map { it.appearance } ?: listOf() }?.contains(AppearanceType.contrast) ?: false

        /// MARK: - Colors
        val colors = this.colors

        colorSpacesComboBox.forEach { (key, combobox) ->
            combobox.selectedIndex = ColorSpace.values().indexOf(colors[key]?.colorSpace ?: ColorSpace.srgb)
        }

        updateVisibles()
    }

    private fun updateVisibles() {
        val idioms = colorSet.colors?.map { it.idiom ?: Idiom.universal } ?: listOf()
        devicePanels.forEach {
            it.value.isVisible = idioms.contains(it.key)
        }

        val colors = this.colors
        colorBoxes.forEach { (key, box) ->
            box.isVisible = colors.containsKey(key)
        }

        colorPanels.forEach { (key, panel) ->
            panel.selectedColor = colors[key]?.color() ?: Color(0, 0, 0, 0)
        }
    }

    private val colors: Map<ColorAssetKey, ColorData>
        get() = colorSet.colors?.associate { colors ->
            Pair(
                Pair(colors.idiom ?: Idiom.universal, colors.appearances?.luminosity),
                Pair(colors.appearances?.highContrast ?: false, colors.gamut)
            ) to colors.color
        } ?: mapOf()

    private fun updateColorSet() {
        if (!canUpdate) return
        val colorSet = colorSet
        val colors: MutableList<Colors> = mutableListOf()
        for (device in Idiom.setValues().filter { deviceCheckboxs[it]?.isSelected ?: false }) {
            for (appearance in selectedAppearances) {
                for (highContrast in selectedConstrants) {
                    for (gamut in selectesGamuts) {
                        val key = Pair(Pair(device, appearance), Pair(highContrast, gamut))
                        val appearances = Appearance.list(highContrast, appearance)
                        var color = colorPanels[key]?.selectedColor?.components()
                        if (color == null || (colorBoxes[key]?.isVisible != true && colorPanels[key]?.selectedColor == Color(0, 0, 0, 0))) {
                            color = colorSet.colors?.firstOrNull()?.color?.components
                        }
                        colors.add(
                            Colors(
                                gamut,
                                device,
                                null,
                                ColorData(
                                    null,
                                    null,
                                    ColorSpace.values()[colorSpacesComboBox[key]?.selectedIndex ?: 0],
                                    color
                                ),
                                if (appearances.isEmpty()) null else appearances,
                            )
                        )
                    }
                }
            }
        }
        colorSet.colors = colors
//        println("${colorSet == this.colorSet}")
//        if (colorSet != this.colorSet) {
        this.colorSet = colorSet
        canUpdate = false
        updateVisibles()
        canUpdate = true
//        }
    }

    private val allAppearances: List<AppearanceValue?>
        get() = listOf(null, AppearanceValue.light, AppearanceValue.dark)

    private val allGamuts: List<Gamut?>
        get() = listOf(null) + Gamut.values()

    private val selectedAppearances: List<AppearanceValue?>
        get() = Appearances.values()[appearancesComboBox?.selectedIndex ?: 0].appearances

    private val selectedConstrants: List<Boolean>
        get() = if (highContrastCheckBox?.isSelected == true) { listOf(false, true) } else { listOf(false) }

    private val selectesGamuts: List<Gamut?>
        get() = Gamuts.values()[gamutComboBox?.selectedIndex ?: 0].gamuts
}

typealias ColorAssetKey = Pair<Pair<Idiom, AppearanceValue?>, Pair<Boolean, Gamut?>>
