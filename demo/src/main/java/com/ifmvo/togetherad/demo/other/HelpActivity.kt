package com.ifmvo.togetherad.demo.other

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ifmvo.togetherad.demo.R
import kotlinx.android.synthetic.main.activity_help.*

/**
 * 采坑指南
 *
 * Created by Matthew Chen on 2020/5/22.
 */
class HelpActivity : AppCompatActivity() {

    companion object {
        fun action(context: Context) {
            context.startActivity(Intent(context, HelpActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        val txt = """
            
广告法要求所有广告必须有广告的标示，否则会有风险。
原生广告需要特别注意这一点，因为原生自渲染需要开发者自己写布局，而开屏、激励、Banner横幅、这类广告平台会自动添加广告标示。

-------------------------------------------

请求到的广告要尽量展示
    ↓
提高展示率、点击率
    ↓
提高 ecpm
    ↓
提高收入

-------------------------------------------

目前穿山甲和广点通的收入相对最好，建议搭配使用，如果两个平台给的量都比较少再用百度补充

-------------------------------------------

错误码: 2001, 错误信息：初始化错误，详细码：200102

如果广点通返回这个错误信息，就是广点通的初始化出现问题，可能参数有问题、也可能项目混淆导致初始化异常

-------------------------------------------

由于会影响 ecpm，所以设置超时时间一定要慎重，尽量设置平台建议的时间范围或使用默认值

-------------------------------------------

错误码: 40029, 错误信息：两种情况：
1. SDK版本低；使用的sdk版本过低，还不支持个性化模板渲染功能。解决办法：升级到平台最新版本sdk。
2. 接口使用错误；创建的代码位类型是模板渲染/非模板渲染，但是请求方法是非模板渲染/模板渲染的方法。
解决办法：使用模板渲染的方法去请求模板渲染类型或者使用非模板渲染的方法去请求非模板类型的广告，如果代码位在平台上是模板渲染，可以参考文档中个性化模板XX广告的部分，demo中参考带有express部分的代码。
如果代码位不是模板渲染，则不要调用含有express字样的接口。参考文档：https://partner.oceanengine.com/doc?id=5dd0fe716b181e00112e3eb8

如果穿山甲报这个错误，请及时联系我

-------------------------------------------

gdt: 请求失败了：错误码: 5004, 错误信息：没有广告

优量汇报这个原因可能是请求太频繁，触发了优量汇的防刷量机制，可以等一个小时再试




        """.trimIndent()
        tvText.text = txt
    }
}