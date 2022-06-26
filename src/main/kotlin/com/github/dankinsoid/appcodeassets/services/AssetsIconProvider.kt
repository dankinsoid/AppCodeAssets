package com.github.dankinsoid.appcodeassets.services

import com.github.dankinsoid.appcodeassets.models.Images
import com.intellij.ide.FileIconProvider
import com.intellij.ide.IconProvider
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ProjectViewNodeDecorator
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.packageDependencies.ui.PackageDependenciesNode
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.ui.ColoredTreeCellRenderer
import java.io.File
import javax.swing.Icon

class AssetsIconProvider: ProjectViewNodeDecorator {

    private val assetsExtension = "xcassets"

    fun getIcon(path: String): Icon {
        return IconLoader.getIcon(path, this.javaClass)
    }

    override fun decorate(node: ProjectViewNode<*>?, data: PresentationData?) {
        if (node?.virtualFile?.extension == assetsExtension) {
            data?.setIcon(getIcon("/icons/assets.svg"))
        }
    }

    override fun decorate(node: PackageDependenciesNode?,renderer: ColoredTreeCellRenderer?) {
    }
}
