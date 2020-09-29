package com.fastnews.service.model

data class CommentResponse(val data: CommentDataChildAfter)

data class CommentDataChildAfter(val children: List<CommentChildren>, val after: String)

data class CommentChildren(val kind: String, val data: CommentData)

data class CommentData(val id: String, val author: String, val body: String, val name: String, val downs: Int, val ups: Int, val created_utc: Long)