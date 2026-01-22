package cn.wj.plugin.vcs.dialog

import cn.wj.plugin.vcs.R
import cn.wj.plugin.vcs.bundle.getString
import cn.wj.plugin.vcs.constants.STORAGE_KEY_COMMIT_MESSAGE
import cn.wj.plugin.vcs.entity.ChangeTypeEntity
import cn.wj.plugin.vcs.entity.CommitMessageEntity
import cn.wj.plugin.vcs.ext.addWithFont
import cn.wj.plugin.vcs.tools.ConfigHelper
import cn.wj.plugin.vcs.tools.GitLogQuery
import cn.wj.plugin.vcs.tools.toJsonString
import cn.wj.plugin.vcs.ui.fillX
import cn.wj.plugin.vcs.ui.migLayout
import cn.wj.plugin.vcs.ui.migLayoutVertical
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBScrollPane
import net.miginfocom.layout.CC
import java.io.File
import java.util.function.Consumer
import javax.swing.ButtonGroup
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.ScrollPaneConstants

/**
 * 提交规范弹窗
 *
 * > [王杰](mailto:w15555650921@gmail.com) 创建于 20201/3/19
 */
class CommitSpecificationDialog(private val project: Project?, message: CommitMessageEntity?) :
    DialogWrapper(project) {

    private val panel = CommitSpecificationPanel(project, message)

    init {
        title = getString(R.String.commit_dialog_title)
        setOKButtonText(getString(R.String.general_confirm))
        init()
    }

    override fun createCenterPanel(): JComponent {
        return panel.createCenterPanel()
    }

    override fun doOKAction() {
        super.doOKAction()
        if (null != project) {
            PropertiesComponent.getInstance(project)
                .setValue(STORAGE_KEY_COMMIT_MESSAGE, getMessageEntity().toJsonString())
        }
    }

    fun getMessageEntity(): CommitMessageEntity {
        return panel.getMessageEntity()
    }
}

/**
 * 规范弹窗面板
 */
class CommitSpecificationPanel(private val project: Project?, private val message: CommitMessageEntity?) {

    private var typeOfChangeGroup: ButtonGroup? = null

    private var scopeOfChangeBox: ComboBox<String>? = null

    private var shortDescriptionField: JTextField? = null

    private var longDescriptionArea: JTextArea? = null

    private var breakingChangesArea: JTextArea? = null

    private var closedIssuesField: JTextField? = null

    fun createCenterPanel(): JComponent {
        val config = ConfigHelper.loadConfig(project)

        val panel = JPanel(migLayout()).apply {
            // 修改类型
            addWithFont(JLabel(getString(R.String.commit_type_of_change)))
            addWithFont(
                JPanel(migLayoutVertical()).apply {
                    typeOfChangeGroup = ButtonGroup()
                    var selected = false
                    config.changeTypes.forEach { changeType ->
                        val rb = JRadioButton(changeType.display()).apply {
                            actionCommand = changeType.action
                            if (message?.typeOfChange?.action == changeType.action) {
                                isSelected = true
                                selected = true
                            }
                        }
                        typeOfChangeGroup!!.add(rb)
                        addWithFont(rb, fillX())
                    }
                    if (!selected) {
                        typeOfChangeGroup!!.elements.toList().firstOrNull()?.isSelected = true
                    }
                },
                fillX().gap("5", "5", "5", "5").wrap()
            )

            // 影响范围
            addWithFont(JLabel(getString(R.String.commit_scope_of_change)), CC())
            scopeOfChangeBox = ComboBox<String>().apply {
                isEditable = true
                addItem("")
//                val path = project?.basePath.orEmpty()
//                val workingDirectory = File(path)
//                val result = GitLogQuery(workingDirectory).execute()
//                if (result.isSuccess()) {
//                    addItem("")
//                    result.getScopes().forEach(
//                        Consumer { item: String? ->
//                            addItem(item)
//                        }
//                    )
//                }
                selectedItem = message?.scopeOfChange.orEmpty()
            }
            addWithFont(scopeOfChangeBox!!, fillX().gap("5", "5", "5", "5").wrap())

            // 简单说明
            addWithFont(JLabel(getString(R.String.commit_short_description)))
            shortDescriptionField = JTextField(message?.shortDescription.orEmpty())
            addWithFont(shortDescriptionField!!, fillX().gap("5", "5", "5", "5").wrap())

            // 详细说明
            addWithFont(JLabel(getString(R.String.commit_long_description)))
            longDescriptionArea = JTextArea(message?.longDescription.orEmpty()).apply {
                lineWrap = true
                wrapStyleWord = true
            }
            addWithFont(longDescriptionArea!!, fillX().gap("5", "5", "5", "5").minWidth("200").minHeight("100").wrap())

            // 重大改变
            addWithFont(JLabel(getString(R.String.commit_breaking_changes)))
            breakingChangesArea = JTextArea(message?.breakingChanges.orEmpty()).apply {
                lineWrap = true
                wrapStyleWord = true
            }
            addWithFont(breakingChangesArea!!, fillX().gap("5", "5", "5", "5").minWidth("200").minHeight("50").wrap())

            // 关闭的问题
            addWithFont(JLabel(getString(R.String.commit_closed_issues)))
            closedIssuesField = JTextField(message?.closedIssues.orEmpty())
            addWithFont(closedIssuesField!!, fillX().gap("5", "5", "5", "5").wrap())

            // 重置按钮
            addWithFont(JButton(getString(R.String.commit_reset)).apply {
                addActionListener {
                    typeOfChangeGroup?.elements?.toList()?.firstOrNull()?.isSelected = true
                    scopeOfChangeBox?.selectedItem = ""
                    shortDescriptionField?.text = null
                    longDescriptionArea?.text = null
                    breakingChangesArea?.text = null
                    closedIssuesField?.text = null
                }
            })
        }
        return JBScrollPane(
            panel,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        )
    }

    fun getMessageEntity(): CommitMessageEntity {
        return CommitMessageEntity(
            typeOfChange = getSelectedChangeType(),
            scopeOfChange = scopeOfChangeBox?.selectedItem.toString(),
            shortDescription = shortDescriptionField?.text.orEmpty().trim { it <= ' ' },
            longDescription = longDescriptionArea?.text.orEmpty().trim { it <= ' ' },
            breakingChanges = breakingChangesArea?.text.orEmpty().trim { it <= ' ' },
            closedIssues = closedIssuesField?.text.orEmpty().trim { it <= ' ' }
        )
    }

    private fun getSelectedChangeType(): ChangeTypeEntity {
        val typeList = ConfigHelper.loadConfig(project).changeTypes
        val selectedButton = typeOfChangeGroup?.elements?.toList()?.firstOrNull {
            it.isSelected
        } ?: return typeList.first()
        return typeList.firstOrNull {
            it.action == selectedButton.actionCommand
        } ?: typeList.first()
    }
}
