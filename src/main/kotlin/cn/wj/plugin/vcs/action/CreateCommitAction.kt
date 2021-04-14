package cn.wj.plugin.vcs.action

import cn.wj.plugin.vcs.dialog.CommitSpecificationDialog
import cn.wj.plugin.vcs.entity.CommitMessageEntity
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.Refreshable

/**
 * 创建提交按钮
 *
 * > [王杰](mailto:w15555650921@gmail.com) 创建于 20201/3/19
 */
class CreateCommitAction :
    DumbAwareAction() {

    init {
        isEnabledInModalContext = true
    }

    override fun actionPerformed(e: AnActionEvent) {

        val cmi = getCommitMessageI(e) ?: return
        val commitMessage = getCommitMessage(cmi, e.project)
        val dialog = CommitSpecificationDialog(e.project, commitMessage)
        dialog.show()

        if (dialog.exitCode == DialogWrapper.OK_EXIT_CODE) {
            cmi.setCommitMessage(dialog.getMessageEntity().getCommitString(e.project))
        }
    }

    private fun getCommitMessageI(e: AnActionEvent): CommitMessageI? {
        val data = Refreshable.PANEL_KEY.getData(e.dataContext)
        return if (data is CommitMessageI) {
            data
        } else {
            VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(e.dataContext)
        }
    }

    private fun getCommitMessage(cmi: CommitMessageI, project: Project?): CommitMessageEntity? {
        return if (cmi is CheckinProjectPanel) {
            val msg = cmi.commitMessage
            CommitMessageEntity.parse(msg, project)
        } else {
            null
        }
    }
}