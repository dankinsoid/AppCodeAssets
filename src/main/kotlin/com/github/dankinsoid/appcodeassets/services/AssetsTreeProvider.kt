package com.github.dankinsoid.appcodeassets.services

import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.TreeStructureProvider
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.ProjectViewProjectNode
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.ide.util.treeView.NodeDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.StatusBarWidget.IconPresentation
import com.intellij.openapi.wm.impl.status.widget.StatusBarWidgetWrapper.Icon
import com.intellij.psi.impl.PsiFileFactoryImpl
import com.intellij.psi.impl.file.PsiDirectoryFactoryImpl
import com.intellij.psi.impl.source.PsiFileImpl
import com.intellij.util.ui.MenuItemCheckIconFactory
import javax.swing.text.StyleConstants.ColorConstants

class AssetsTreeProvider: TreeStructureProvider {

    private val assetsExtension = "xcassets"

    override fun modify(
        parent: AbstractTreeNode<*>,
        children: MutableCollection<AbstractTreeNode<*>>,
        settings: ViewSettings?
    ): MutableCollection<AbstractTreeNode<*>> {

        if (parent is ProjectViewNode) {
            val file = parent.virtualFile ?: return children
            if (file.extension == assetsExtension) {
                return xcassets(file, parent.project, settings)
            }
        }
        if (parent is PsiDirectoryNode) {
            val file = parent.virtualFile ?: return children
            if (file.extension == assetsExtension) {
                return xcassets(file, parent.project, settings)
            }
        }
        return children
    }

    private fun xcassets(
        file: VirtualFile,
        project: Project,
        settings: ViewSettings?
    ): MutableCollection<AbstractTreeNode<*>> {
        val nodes = ArrayList<AbstractTreeNode<*>>()
        file.children.filter {
            it.extension?.isEmpty() ?: true || Extensions.contains(it.extension)
        }.forEach {
            val directory = PsiDirectoryFactoryImpl.getInstance(project).createDirectory(it)
            val node = PsiDirectoryNode(project, directory, settings)
            nodes.add(node)
        }
        return nodes
    }
}