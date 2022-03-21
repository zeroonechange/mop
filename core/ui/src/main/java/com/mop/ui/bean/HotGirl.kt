package com.mop.ui.bean

data class HotGirl(
    val total: Int,
    val total_pages: Int,
    val results: List<HotGirlDetail>
)

data class HotGirlDetail(
    val id: String,
    val created_at: String,
    val updated_at: String,
    val promoted_at: String,
    val width: Int,
    val height: Int,
    val color: String,
    val blur_hash: String,
    val description: String,
    val alt_description: String,
    val urls: Urls,
    val categories: List<Any>,
    val likes: Int,
    val liked_by_user: Boolean,
    val current_user_collections: List<Any>,
    val sponsorship: Any,
    val user: User,
    val tags: List<Tag>
)


data class Tag(
    val type: String,
    val title: String,
    val source: Source
)


data class Source(
    val ancestry: Ancestry,
    val title: String,
    val subtitle: String,
    val description: String,
    val meta_title: String,
    val meta_description: String,
    val cover_photo: CoverPhoto
)

data class Ancestry(
    val type: Type,
    val category: Category,
    val subcategory: Subcategory
)

data class CoverPhoto(
    val id: String,
    val created_at: String,
    val updated_at: String,
    val promoted_at: String,
    val width: Int,
    val height: Int,
    val color: String,
    val blur_hash: String,
    val description: String,
    val alt_description: String,
    val urls: UrlsX,
    val links: LinksXX,
    val categories: List<Any>,
    val likes: Int,
    val liked_by_user: Boolean,
    val current_user_collections: List<Any>,
    val sponsorship: Any,
    val user: UserX
)

data class Type(
    val slug: String,
    val pretty_slug: String
)

data class Category(
    val slug: String,
    val pretty_slug: String
)

data class Subcategory(
    val slug: String,
    val pretty_slug: String
)

data class UrlsX(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String,
    val small_s3: String
)

data class LinksXX(
    val self: String,
    val html: String,
    val download: String,
    val download_location: String
)

data class UserX(
    val id: String,
    val updated_at: String,
    val username: String,
    val name: String,
    val first_name: String,
    val last_name: String,
    val twitter_username: String,
    val portfolio_url: Any,
    val bio: String,
    val location: String,
    val links: LinksXXX,
    val profile_image: ProfileImageX,
    val instagram_username: String,
    val total_collections: Int,
    val total_likes: Int,
    val total_photos: Int,
    val accepted_tos: Boolean,
    val for_hire: Boolean,
    val social: SocialX
)

data class CurrentEvents(
    val status: String,
    val approved_on: String
)

data class PeopleX(
    val status: String,
    val approved_on: String
)

data class LinksXXX(
    val self: String,
    val html: String,
    val photos: String,
    val likes: String,
    val portfolio: String,
    val following: String,
    val followers: String
)

data class ProfileImageX(
    val small: String,
    val medium: String,
    val large: String
)

data class SocialX(
    val instagram_username: String,
    val portfolio_url: Any,
    val twitter_username: String,
    val paypal_email: Any
)