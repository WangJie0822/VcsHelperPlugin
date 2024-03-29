package cn.wj.plugin.vcs.storage

import cn.wj.plugin.vcs.constants.DEFAULT_AUTO_WRAP_LENGTH
import cn.wj.plugin.vcs.constants.DEFAULT_AUTO_WRAP_LENGTH_INT
import cn.wj.plugin.vcs.constants.DEFAULT_BREAKING_CHANGES
import cn.wj.plugin.vcs.constants.DEFAULT_BREAKING_CHANGES_WHEN_EMPTY
import cn.wj.plugin.vcs.constants.DEFAULT_CLOSED_ISSUES
import cn.wj.plugin.vcs.constants.DEFAULT_CLOSED_ISSUES_SEPARATOR
import cn.wj.plugin.vcs.constants.DEFAULT_CLOSED_ISSUES_WHEN_EMPTY
import cn.wj.plugin.vcs.constants.DEFAULT_DESCRIPTION_SEPARATOR
import cn.wj.plugin.vcs.constants.DEFAULT_FONT_NAME
import cn.wj.plugin.vcs.constants.DEFAULT_JSON_CONFIG_PATH
import cn.wj.plugin.vcs.constants.DEFAULT_REAR_SCOPE
import cn.wj.plugin.vcs.constants.DEFAULT_SCOPE_WRAPPER_END
import cn.wj.plugin.vcs.constants.DEFAULT_SCOPE_WRAPPER_START
import cn.wj.plugin.vcs.constants.DEFAULT_TEXT_AUTO_WRAP
import cn.wj.plugin.vcs.constants.DEFAULT_TYPE_OF_CHANGE_LIST_JSON
import cn.wj.plugin.vcs.constants.DEFAULT_USE_JSON_CONFIG
import cn.wj.plugin.vcs.entity.ChangeTypeEntity
import cn.wj.plugin.vcs.entity.KeywordsEntity
import cn.wj.plugin.vcs.entity.PanelInfoEntity
import cn.wj.plugin.vcs.tools.toTypeEntity
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.CollectionBean

/**
 * 配置数据持久存储服务
 *
 * > [王杰](mailto:w15555650921@gmail.com) 创建于 20201/3/31
 */
@State(name = STORAGE_NAME_OPTIONS, storages = [(Storage("$STORAGE_NAME_OPTIONS.xml"))])
class Options : PersistentStateComponent<Options> {

    /** 是否使用 json 文件配置 */
    var useJsonConfig = DEFAULT_USE_JSON_CONFIG

    /** 配置文件路径 */
    var jsonConfigPath = DEFAULT_JSON_CONFIG_PATH

    /** 是否自动换行 */
    var textAutoWrap = DEFAULT_TEXT_AUTO_WRAP

    /** 单行长度 */
    var autoWrapLength = DEFAULT_AUTO_WRAP_LENGTH

    /** 输入框字体名 */
    var inputTextFontName = DEFAULT_FONT_NAME

    /** 影响范围包裹符号左 */
    var scopeWrapperStart = DEFAULT_SCOPE_WRAPPER_START

    /** 影响范围包裹符号右 */
    var scopeWrapperEnd = DEFAULT_SCOPE_WRAPPER_END

    /** 简单描述分隔符 */
    var descriptionSeparator = DEFAULT_DESCRIPTION_SEPARATOR

    /** 重大改变关键字 */
    var breakingChanges = DEFAULT_BREAKING_CHANGES

    /** 重大改变为空显示 */
    var breakingChangesWhenEmpty = DEFAULT_BREAKING_CHANGES_WHEN_EMPTY

    /** 关闭的问题关键字 */
    var closedIssues = DEFAULT_CLOSED_ISSUES

    /** 关闭的问题分隔符 */
    var closedIssuesSeparator = DEFAULT_CLOSED_ISSUES_SEPARATOR

    /** 关闭的问题为空显示 */
    var closedIssuesWhenEmpty = DEFAULT_CLOSED_ISSUES_WHEN_EMPTY

    /** 影响范围后置 */
    var rearScope = DEFAULT_REAR_SCOPE

    /** 修改类型列表 */
    @CollectionBean
    var typeOfChangeList = DEFAULT_TYPE_OF_CHANGE_LIST_JSON

    fun getTypeOfChangeList(): ArrayList<ChangeTypeEntity> {
        return typeOfChangeList.toTypeEntity() ?: arrayListOf()
    }

    fun toPanelEntity(): PanelInfoEntity {
        return PanelInfoEntity(
            keywords = KeywordsEntity(
                wrapWords = textAutoWrap,
                maxLineLength = autoWrapLength.toIntOrNull() ?: DEFAULT_AUTO_WRAP_LENGTH_INT,
                scopeWrapperStart = scopeWrapperStart,
                scopeWrapperEnd = scopeWrapperEnd,
                descriptionSeparator = descriptionSeparator,
                breakingChanges = breakingChanges,
                breakingChangesEmpty = breakingChangesWhenEmpty,
                closedIssues = closedIssues,
                closedIssuesSeparator = closedIssuesSeparator,
                closedIssuesEmpty = closedIssuesWhenEmpty
            ),
            typeOfChangeList.toTypeEntity<ArrayList<ChangeTypeEntity>>() ?: arrayListOf()
        )
    }

    override fun getState(): Options {
        return this
    }

    override fun loadState(state: Options) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {

        /** 单例对象 */
        val instance: Options by lazy {
            ServiceManager.getService(Options::class.java)
        }
    }
}

const val STORAGE_NAME_OPTIONS = "cn.wj.plugin.vcs.storage.options"
