package com.manjil.movieapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherPojo(

    @field:SerializedName("country_code")
    val countryCode: String? = null,

    @field:SerializedName("city_name")
    val cityName: String? = null,

    @field:SerializedName("data")
    val data: List<DataItem?>? = null,

    @field:SerializedName("timezone")
    val timezone: String? = null,

    @field:SerializedName("lon")
    val lon: Any? = null,

    @field:SerializedName("state_code")
    val stateCode: String? = null,

    @field:SerializedName("lat")
    val lat: Any? = null
) : Serializable

data class DataItem(

    @field:SerializedName("pres")
    val pres: Any? = null,

    @field:SerializedName("moon_phase")
    val moonPhase: Any? = null,

    @field:SerializedName("wind_cdir")
    val windCdir: String? = null,

    @field:SerializedName("moonrise_ts")
    val moonriseTs: Double? = null,

    @field:SerializedName("clouds")
    val clouds: Double? = null,

    @field:SerializedName("low_temp")
    val lowTemp: Any? = null,

    @field:SerializedName("wind_spd")
    val windSpd: Any? = null,

    @field:SerializedName("ozone")
    val ozone: Any? = null,

    @field:SerializedName("pop")
    val pop: Double? = null,

    @field:SerializedName("datetime")
    val datetime: String? = null,

    @field:SerializedName("valid_date")
    val validDate: String? = null,

    @field:SerializedName("precip")
    val precip: Any? = null,

    @field:SerializedName("min_temp")
    val minTemp: Any? = null,

    @field:SerializedName("sunrise_ts")
    val sunriseTs: Double? = null,

    @field:SerializedName("weather")
    val weather: Weather? = null,

    @field:SerializedName("app_max_temp")
    val appMaxTemp: Any? = null,

    @field:SerializedName("max_temp")
    val maxTemp: Any? = null,

    @field:SerializedName("snow_depth")
    val snowDepth: Double? = null,

    @field:SerializedName("max_dhi")
    val maxDhi: Any? = null,

    @field:SerializedName("sunset_ts")
    val sunsetTs: Double? = null,

    @field:SerializedName("clouds_mid")
    val cloudsMid: Double? = null,

    @field:SerializedName("uv")
    val uv: Any? = null,

    @field:SerializedName("vis")
    val vis: Any? = null,

    @field:SerializedName("high_temp")
    val highTemp: Any? = null,

    @field:SerializedName("temp")
    val temp: Double? = null,

    @field:SerializedName("clouds_hi")
    val cloudsHi: Double? = null,

    @field:SerializedName("app_min_temp")
    val appMinTemp: Any? = null,

    @field:SerializedName("moon_phase_lunation")
    val moonPhaseLunation: Any? = null,

    @field:SerializedName("dewpt")
    val dewpt: Any? = null,

    @field:SerializedName("wind_dir")
    val windDir: Double? = null,

    @field:SerializedName("wind_gust_spd")
    val windGustSpd: Any? = null,

    @field:SerializedName("clouds_low")
    val cloudsLow: Double? = null,

    @field:SerializedName("rh")
    val rh: Double? = null,

    @field:SerializedName("slp")
    val slp: Any? = null,

    @field:SerializedName("snow")
    val snow: Double? = null,

    @field:SerializedName("wind_cdir_full")
    val windCdirFull: String? = null,

    @field:SerializedName("moonset_ts")
    val moonsetTs: Double? = null,

    @field:SerializedName("ts")
    val ts: Double? = null
) : Serializable

data class Weather(

    @field:SerializedName("code")
    val code: Double? = null,

    @field:SerializedName("icon")
    val icon: String? = null,

    @field:SerializedName("description")
    val description: String? = null
) : Serializable
