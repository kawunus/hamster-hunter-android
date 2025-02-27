import ru.practicum.android.diploma.core.data.network.dto.Response

class CountriesResponse : Response() {
    var countriesList: List<CountryDto> = emptyList()
}
