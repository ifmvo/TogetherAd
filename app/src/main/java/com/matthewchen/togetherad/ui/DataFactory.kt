package com.matthewchen.togetherad.ui

import com.matthewchen.togetherad.bean.IndexBean

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2019-11-27.
 */
object DataFactory {

    fun getIndexDatas(currentPage: Int): List<IndexBean> {
        val listData = mutableListOf<IndexBean>()
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 1}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 2}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 3}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 4}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 5}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 6}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 7}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 8}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 9}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 10}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 11}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 12}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 13}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 14}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 15}",
                "xxxxx"
            )
        )
        return listData
    }

}