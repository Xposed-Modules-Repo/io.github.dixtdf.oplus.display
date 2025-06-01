package io.github.dixtdf.oplus.display

import android.util.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class DpHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        val oplusConnectingDisplayExImplClazz = XposedHelpers.findClass(
            "com.oplus.systemui.oplusstatusbar.display.OplusConnectingDisplayExImpl",
            lpparam!!.classLoader
        )
        val oplusConnectingDisplayExImplShowDialogClazz = XposedHelpers.findClass(
            "com.oplus.systemui.oplusstatusbar.display.OplusConnectingDisplayExImpl\$showDialog\$1",
            lpparam.classLoader
        )
        val pendingDisplayClass = XposedHelpers.findClass(
            "com.android.systemui.display.domain.interactor.ConnectedDisplayInteractor\$PendingDisplay",
            lpparam.classLoader
        )
        var cachedShowDialog: Any? = null

        XposedHelpers.findAndHookMethod(
            oplusConnectingDisplayExImplClazz,
            "showDialog",
            pendingDisplayClass,
            Boolean::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    cachedShowDialog?.let { cached ->
                        try {
                            val method = cached.javaClass.getDeclaredMethod(
                                "onSelected",
                                Int::class.javaPrimitiveType,
                                Boolean::class.javaPrimitiveType
                            )
                            method.isAccessible = true
                            method.invoke(cached, -1, true)
                            XposedBridge.log("成功调用 cache.onSelected(-1, true)")
                        } catch (e: Exception) {
                            XposedBridge.log("调用 onSelected 失败: ${e.message}")
                        } finally {
                            cachedShowDialog = null
                        }
                    } ?: run {
                        XposedBridge.log("cachedShowDialog 实例未准备好")
                    }
                }
            }
        )

        XposedHelpers.findAndHookConstructor(
            oplusConnectingDisplayExImplShowDialogClazz,
            oplusConnectingDisplayExImplClazz,
            object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                    Log.d("Xposed", "构造函数已创建实例")
                    cachedShowDialog = param.thisObject
                }
            })
    }

}