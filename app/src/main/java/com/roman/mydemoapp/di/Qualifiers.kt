package com.roman.mydemoapp.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AnthropicApiKey

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class YoutubeApiKey
