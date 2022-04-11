package com.example.weatherable

import javax.inject.Inject

interface ResourceProvider {
    val wrong: Pair<String, String>
    val init: Pair<String, String>
    val hi: String
    val error: String
    val single: String
    val start: String
    val joke: String
    val channel: String
    val name: String
    val key: String
    val jokeUrl: String
    val animeUrl: String
    val memesUrl: String

    class Base @Inject constructor(): ResourceProvider {
        override val wrong by lazy { Pair("Wrong token", "") }
        override val init by lazy { Pair("", "") }
        override val hi by lazy { "Hi there!" }
        override val error by lazy { "Error. Try again." }
        override val single by lazy { "single" }
        override val start by lazy { "start" }
        override val joke by lazy { "joke" }
        override val channel by lazy { "Bot_channel" }
        override val name by lazy { "Bluetooth" }
        override val key by lazy { "key" }
        override val jokeUrl by lazy { "https://v2.jokeapi.dev/joke/" }
        override val animeUrl by lazy { "https://api.jikan.moe/" }
        override val memesUrl by lazy { "https://api.imgflip.com" }
    }
}

