# 酷欧天气协程版 #

# 一句话总结 #
## 协程是什么 ##
1. 协程是一种编程思想，不局限于语言。

## Kotlin协程在Android中有什么用？ ##
1. 用同步的方式写异步代码。使得代码更优雅。

# 延伸阅读， 学习协程 #
https://mp.weixin.qq.com/s/Tv-jEjJAn_gZ_M3qBG4Azw

# 欧酷天气-协程MVVM版本 #
## 简介 ##
学习一个新东西，最好的就是找一个项目练练手了。早一阵子学习了郭神coolweatherjetpack这个项目，发现郭神的这个项目显得不是那么的MVVM，在View层做了很多逻辑操作。这样还是会显得View层的比较臃肿。于是站在巨人的肩膀上，酷欧天气，协程MVVM版本就这样诞生了，整体思想基于郭神的coolweatherjetpack的版本，甚至很多代码比如页面布局我都是直接Copy郭神的。再次感谢郭神。
## 项目运行截图 ##


![](https://user-gold-cdn.xitu.io/2019/10/29/16e15a00df99ddda?w=1080&h=2160&f=png&s=2246849)


## 赶快clone下来看看吧，如果这个项目帮到了你，希望能给我一个 star ##
## 代码粗略讲解，以Weather模块为例 ##
### viewModel ###
```kotlin
val weather = MutableLiveData<Weather>()
fun fetchWeather(weatherId: String) {
    // launch 表示开启一个协程环境，类似于JS中的Async
    launch{
        val fetchWeather = repository.fetchWeather(weatherId, MainActivity.KEY) // 这里是运行在IO线程中，进行网络请求
        weather.value = fetchWeather.weather!![0] // 这里是主线程了，用于视图更新
    }
}
```

### WeatherRepository ###
1. viewModel 中调用了一个用 suspend 标记的方法，其中方法体中执行了request方法。下面让我们看下request方法。
```kotlin
suspend fun fetchWeather(weatherId: String, key: String) = request{ service.getWeather(weatherId, key) }
```
2. BaseRepository是所有Repository的基类，里面有用 suspend 关键字标记的request方法，并且用WithContext指定此方法运行在 IO 环境中。
```kotlin
open class BaseRepository {
    suspend fun <T> request(block: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            block()
        }
    }
}
```
3. 连起来看就是表示，在 IO 线程中，通过网络请求到数据，并且返回。

### WeatherActivity ###
1.在Activity中订阅了ViewModel中的liveData,用于视图更新。
```kotlin
weather.observe(this@WeatherActivity, Observer{
    swipeRefresh.isRefreshing = false
    showWeatherInfo(it)
})
```

# 大感谢 #
- https://juejin.im/post/5d0afe0bf265da1b7152fb00  教你用协程。
- https://github.com/guolindev/coolweatherjetpack 郭神
- https://github.com/lulululbj/wanandroid  本文实战项目就是学习这个项目来写的。

# 求star #
https://github.com/LoverJoker/coolweathercoroutines



