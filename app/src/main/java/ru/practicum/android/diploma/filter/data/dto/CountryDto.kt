import com.google.gson.annotations.SerializedName

data class CountryDto(
    val id: String,
    @SerializedName("parent_id")
    val parentId: String?,
    val name: String?,
)
