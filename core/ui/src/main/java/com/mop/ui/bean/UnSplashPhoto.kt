package com.mop.ui.bean

data class UnSplashPhoto(var data: ArrayList<UnSplashPhotoItem>)

data class UnSplashPhotoItem(
    val alt_description: Any,
    val blur_hash: String,
    val categories: List<Any>,
    val color: String,
    val created_at: String,
    val current_user_collections: List<Any>,
    val description: String,
    val downloads: Int,
    val exif: Exif,
    val height: Int,
    val id: String,
    val liked_by_user: Boolean,
    val likes: Int,
    val links: Links,
    val location: Location,
    val promoted_at: String,
    val sponsorship: Any,
    val topic_submissions: TopicSubmissions,
    val updated_at: String,
    val urls: Urls,
    val user: User,
    val views: Int,
    val width: Int
)

data class Exif(
    val aperture: String,
    val exposure_time: String,
    val focal_length: String,
    val iso: Int,
    val make: String,
    val model: String,
    val name: String
)

data class Links(
    val download: String,
    val download_location: String,
    val html: String,
    val self: String
)

data class Location(
    val city: Any,
    val country: Any,
    val name: Any,
    val position: Position,
    val title: Any
)

class TopicSubmissions

data class Urls(
    val full: String,
    val raw: String,
    val regular: String,
    val small: String,
    val small_s3: String,
    val thumb: String
)

data class User(
    val accepted_tos: Boolean,
    val bio: Any,
    val first_name: String,
    val for_hire: Boolean,
    val id: String,
    val instagram_username: String,
    val last_name: String,
    val links: LinksX,
    val location: Any,
    val name: String,
    val portfolio_url: Any,
    val profile_image: ProfileImage,
    val social: Social,
    val total_collections: Int,
    val total_likes: Int,
    val total_photos: Int,
    val twitter_username: String,
    val updated_at: String,
    val username: String
)

data class Position(
    val latitude: Any,
    val longitude: Any
)

data class LinksX(
    val followers: String,
    val following: String,
    val html: String,
    val likes: String,
    val photos: String,
    val portfolio: String,
    val self: String
)

data class ProfileImage(
    val large: String,
    val medium: String,
    val small: String
)

data class Social(
    val instagram_username: String,
    val paypal_email: Any,
    val portfolio_url: Any,
    val twitter_username: String
)