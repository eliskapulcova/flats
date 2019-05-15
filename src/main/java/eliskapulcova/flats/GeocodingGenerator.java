package eliskapulcova.flats;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import eliskapulcova.flats.entity.AdDetail;
import eliskapulcova.flats.repository.AdDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeocodingGenerator {

    private final AdDetailRepository adDetailRepository;

    @Autowired
    public GeocodingGenerator(AdDetailRepository adDetailRepository) {
        this.adDetailRepository = adDetailRepository;
    }

    public void run() throws InterruptedException, IOException, ApiException {

        Iterable<AdDetail> adDetails = adDetailRepository.findAll();

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyAb6zGH2eJv_WjxqAEFh_GvJZnqc3R38xw")
                .build();

        int i = 0;
        for (AdDetail adDetail : adDetails) {
            process(context, adDetail);
            i++;
            if (i == 3) {
                break;
            }
        }
    }

    private void process(GeoApiContext context, AdDetail adDetail) throws InterruptedException, ApiException, IOException {
        String location = adDetail.getLocation();

        GeocodingResult[] geocodingResults = GeocodingApi.geocode(context, location).await();

        LatLng flatLocation = geocodingResults[0].geometry.location;

        System.out.println(flatLocation);
        System.out.println(geocodingResults[0].formattedAddress);


        PlacesSearchResult[] busStationSearchResults = PlacesApi.nearbySearchQuery(context, flatLocation)
                .type(PlaceType.BUS_STATION)
                .rankby(RankBy.DISTANCE)
                .await()
                .results;


        LatLng busStationLocation = busStationSearchResults[0].geometry.location;

        processStationSearchResult(context, flatLocation, busStationLocation);


        PlacesSearchResult[] trainStationSearchResults = PlacesApi.nearbySearchQuery(context, flatLocation)
                .type(PlaceType.TRAIN_STATION)
                .rankby(RankBy.DISTANCE)
                .await()
                .results;

        LatLng trainStationLocation = trainStationSearchResults[0].geometry.location;

        processStationSearchResult(context, flatLocation, trainStationLocation);


        PlacesSearchResult[] subwayStationSearchResults = PlacesApi.nearbySearchQuery(context, flatLocation)
                .type(PlaceType.SUBWAY_STATION)
                .rankby(RankBy.DISTANCE)
                .await()
                .results;


        LatLng subwayStationLocation = subwayStationSearchResults[0].geometry.location;

        processStationSearchResult(context, flatLocation, subwayStationLocation);

        PlacesSearchResult[] lightRailStationSearchResults = PlacesApi.nearbySearchQuery(context, flatLocation)
                .type(PlaceType.LIGHT_RAIL_STATION)
                .rankby(RankBy.DISTANCE)
                .await()
                .results;


        LatLng lightRailStationLocation = lightRailStationSearchResults[0].geometry.location;

        processStationSearchResult(context, flatLocation, lightRailStationLocation);
    }

    private void processStationSearchResult(GeoApiContext context, LatLng flatLocation, LatLng stopStationLocation) throws ApiException, InterruptedException, IOException {
        DirectionsResult directionsResult = DirectionsApi.getDirections(
                context,
                flatLocation.toString(),
                stopStationLocation.toString()
        ).mode(TravelMode.WALKING)
                .await();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(directionsResult));

        DirectionsRoute[] distanceRoute = directionsResult.routes;
        Long distance = distanceRoute[0].legs[0].distance.inMeters;
        System.out.println(distance);
    }
}

