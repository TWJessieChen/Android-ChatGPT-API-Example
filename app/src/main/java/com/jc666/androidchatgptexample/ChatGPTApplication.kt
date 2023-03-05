package com.jc666.androidchatgptexample

import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import com.jc666.androidchatgptexample.utils.xLog
import kotlinx.coroutines.*
import leakcanary.AppWatcher
import leakcanary.LeakCanary

/**
 * Application()初始建置
 *
 * @author JC666
 */

class ChatGPTApplication  : Application() {
    private val TAG = ChatGPTApplication::class.java.simpleName

    val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        xLog.d(TAG, "onCreate")

        appContentResolver = contentResolver
        appContext = applicationContext

        registerOnActivity(this)

        if(BuildConfig.DEBUG) {
            setupLeakCanary()
        }
    }

    private fun setupLeakCanary() {
        if (!AppWatcher.isInstalled) {
            AppWatcher.manualInstall(
                application = this,
                watchersToInstall = AppWatcher.appDefaultWatchers(this),
            )
        }

        LeakCanary.showLeakDisplayActivityLauncherIcon(true)
        LeakCanary.config = LeakCanary.config.copy(dumpHeap = true)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        applicationScope.cancel()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            xLog.d(TAG, "application background!!!")
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        xLog.d(TAG, "onConfigurationChanged")
    }

    class registerOnActivity(val application: Application) {
        private val callback = object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                //xLog.d(TAG, " ActivityLifecycleCallbacks -> onActivityCreated")
                if(BuildConfig.DEBUG) {
                    AppWatcher.objectWatcher.expectWeaklyReachable(
                        activity,
                        "${activity::class.java.name} received Activity#onDestroy() callback"
                    )
                }
            }
            override fun onActivityStarted(activity: Activity) {
                //xLog.d(TAG, " ActivityLifecycleCallbacks -> onActivityStarted")
            }
            override fun onActivityResumed(activity: Activity)  {
                //xLog.d(TAG, " ActivityLifecycleCallbacks -> onActivityResumed")
            }
            override fun onActivityPaused(activity: Activity)  {
                //xLog.d(TAG, " ActivityLifecycleCallbacks -> onActivityPaused : " + activity)
            }
            override fun onActivityStopped(activity: Activity)  {
                //xLog.d(TAG, " ActivityLifecycleCallbacks -> onActivityStopped")
            }
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle)  {
                //xLog.d(TAG, " ActivityLifecycleCallbacks -> onActivitySaveInstanceState")
            }
            override fun onActivityDestroyed(activity: Activity)  {
                //xLog.d(TAG, " ActivityLifecycleCallbacks -> onActivityDestroyed")
            }
        }

        fun register() {
            application.registerActivityLifecycleCallbacks(callback)
        }

        init {
            register()
        }

    }

    companion object {
        private val TAG = ChatGPTApplication::class.java.simpleName

        var appContentResolver: ContentResolver? = null
            private set
        var appContext: Context? = null
            private set


    }

}