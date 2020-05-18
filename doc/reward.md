# 激励广告

详情查看下面示例和注释：

> 展示广告一定要在 onAdLoaded() 回调方法中执行

```
class RewardActivity : AppCompatActivity() {

    private lateinit var adHelperReward: AdHelperReward

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward)

        //使用 Map<String, Int> 配置广告商 权重，通俗的讲就是 随机请求的概率占比
        val radioMapReward = mapOf(
                AdProviderType.GDT.type to 3,
                AdProviderType.CSJ.type to 1
                //AdProviderType.BAIDU.type to 1
        )

        /**
         * activity: 必传。
         * alias: 必传。广告位的别名。初始化的时候是根据别名设置的广告ID，所以这里TogetherAd会根据别名查找对应的广告位ID。
         * radioMap: 非必传。广告商的权重。可以不传或传null，空的情况 TogetherAd 会自动使用初始化时 TogetherAd.setPublicProviderRadio 设置的全局通用权重。
         * listener: 非必传。如果你不需要监听结果可以不传或传空。各个回调方法也可以选择性添加
         */
        adHelperReward = AdHelperReward(activity = this, alias = TogetherAdAlias.AD_REWARD, radioMap = radioMapReward, listener = object : RewardListener {
            override fun onAdStartRequest(providerType: String) {
                //在开始请求之前会回调此方法，失败切换的情况会回调多次
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                //请求失败的回调，失败切换的情况会回调多次
            }

            override fun onAdFailedAll(failedMsg: String?) {
                //所有配置的广告商都请求失败了，只有在全部失败之后会回调一次
            }

            override fun onAdClicked(providerType: String) {
                //点击广告的回调
            }

            override fun onAdShow(providerType: String) {
                //广告展示展示的回调
            }

            override fun onAdLoaded(providerType: String) {
                //广告请求成功的回调，每次请求只回调一次
                //展示广告一定要在 onAdLoaded() 回调方法中执行
                adHelperReward.show()
            }

            override fun onAdExpose(providerType: String) {
                //广告展示曝光的回调
            }

            override fun onAdVideoComplete(providerType: String) {
                //视频播放完成的回调
            }

            override fun onAdVideoCached(providerType: String) {
                //视频缓存完成的回调
            }

            override fun onAdRewardVerify(providerType: String) {
                //激励结果验证成功的回调
            }

            override fun onAdClose(providerType: String) {
                //广告被关闭的回调
            }
        })

        //开始请求广告
        adHelperReward.load()
    }
}
```

可查看 Demo 中 [激励广告的示例代码](../demo/src/main/java/com/ifmvo/togetherad/demo/reward/RewardActivity.kt)