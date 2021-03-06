package com.jess.wodtimer.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.jess.wodtimer.BR
import com.jess.wodtimer.common.extension.createViewModel
import com.jess.wodtimer.common.view.dialog.ProgressDialog
import kotlin.reflect.KClass

/**
 * @author jess
 * @since 2020.08.11
 */
abstract class BaseActivity<VD : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {

    // ViewDataBinding
    private lateinit var binding: VD

    // 레이아웃 ID
    protected abstract val layoutRes: Int

    // ViewModel Class
    protected abstract val viewModelClass: KClass<VM>

    // AAC ViewModel
    protected val vm by lazy {
        createViewModel(viewModelClass)
    }

    protected val progressDialog by lazy {
        ProgressDialog(this)
    }

    // 레이아웃 초기화
    abstract fun initLayout()

    // onCreate 완료
    abstract fun onCreated(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        initLayout()
        onCreated(savedInstanceState)
    }

    /**
     * 데이터 바인딩 초기화
     */
    protected open fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.run {
            lifecycleOwner = this@BaseActivity
            setVariable(BR.vm, vm)
        }

        vm.isProgress?.observe(this, Observer {
            if (it) progressDialog.show() else progressDialog.dismiss()
        })
    }
}
