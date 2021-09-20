package com.example.melearning.fragments.paging

import com.example.melearning.examples.ImageInfo
import com.example.melearning.examples.PostInfo

data class PostData(var postInfo: PostInfo = PostInfo(),
                    var imageInfo: ImageInfo = ImageInfo(),
                    var order: Int = -1)