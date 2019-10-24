package com.jokerliang.coolweathercoroutines.ui.area

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import com.jokerliang.coolweathercoroutines.R
import com.jokerliang.coolweathercoroutines.base.BaseFragment
import com.jokerliang.coolweathercoroutines.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.choose_area.*

/**
求贤若饥 虚心若愚
 * @author jokerliang
 */
class ChooseAreaFragment: BaseFragment<ChooseAreaViewModel>() {
    private lateinit var adapter: ArrayAdapter<String>
    private var progressDialog: ProgressDialog? = null

    override fun getLayoutResId(): Int = R.layout.choose_area

    override fun providerVMClass(): Class<ChooseAreaViewModel>? = ChooseAreaViewModel::class.java

    override fun initView() {
        adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, mViewModel.dataList.value)
        listView.adapter = adapter
    }

    override fun initData() {
        queryProvinces()
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                mViewModel.clickPosition = position
            }
        backButton.setOnClickListener {
            mViewModel.apply {
                when(currentLevel) {
                    LEVEL_CITY -> queryProvinces()
                    LEVEL_COUNTY -> queryCity()
                }
            }
        }
    }

    private fun queryProvinces() {
        mViewModel.queryProvinces()
    }

    override fun onError(e: Throwable) {
        Toast.makeText(activity, "出现了错误${e.message}", Toast.LENGTH_SHORT).show()
        closeProgressDialog()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel.apply {
            uiState.observe(this@ChooseAreaFragment, Observer {
                it.apply {
                    titleText.text = title

                    backButton.visibility = if (backButtonVisibility) View.VISIBLE else View.INVISIBLE

                    if (clickCounty()) {
                        if (activity is WeatherActivity) {
                            val weatherActivity = activity as WeatherActivity
                            weatherActivity.drawerLayout.closeDrawers()
                            weatherActivity.swipeRefresh.isRefreshing = true
                            weatherActivity.refreshWeather(weatherId)
                        } else {
                            mViewModel.setEnterFlag(weatherId)
                            val intent = Intent(activity, WeatherActivity::class.java)
                            intent.putExtra("weather_id", weatherId)
                            startActivity(intent)
                            activity?.finish()
                        }

                    }

                    if (isLoading) showProgressDialog()
                }
            })

            dataList.observe(this@ChooseAreaFragment, Observer {
                closeProgressDialog()
                adapter.clear()
                adapter.addAll(it)
                adapter.notifyDataSetChanged()
            })
        }

    }

    /**
     * 这里是直接Copy郭神的
     * 显示进度对话框
     */
    private fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(activity)
            progressDialog?.setMessage("正在加载...")
            progressDialog?.setCanceledOnTouchOutside(false)
        }
        progressDialog?.show()
    }

    /**
     * 关闭进度对话框
     */
    private fun closeProgressDialog() {
        progressDialog?.dismiss()
    }

    companion object {
        const val LEVEL_PROVINCE = 0
        const val LEVEL_CITY = 1
        const val LEVEL_COUNTY = 2
    }
}