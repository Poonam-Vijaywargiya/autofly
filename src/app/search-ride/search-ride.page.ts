import { Component, ElementRef, NgZone, ViewChild, OnInit } from '@angular/core';
import { Geolocation } from '@ionic-native/geolocation/ngx';
import { Router } from '@angular/router';
import { NativeStorage } from '@ionic-native/native-storage/ngx';
import { GooglePlus } from '@ionic-native/google-plus/ngx';
import { LoadingController, NavController, Platform } from '@ionic/angular';
import { getDirectivesAtNodeIndex } from '@angular/core/src/render3/context_discovery';

declare var google: any;
@Component({
  selector: 'app-search-ride',
  templateUrl: './search-ride.page.html',
  styleUrls: ['./search-ride.page.scss'],
})
export class SearchRidePage {

  constructor(public zone: NgZone, public geolocation: Geolocation, private googlePlus: GooglePlus,
    private nativeStorage: NativeStorage,
    public loadingController: LoadingController,
    private router: Router) {
    /*load google map script dynamically */
    const script = document.createElement('script');
    script.id = 'googleMap';
    if (this.apiKey) {
      script.src = 'https://maps.googleapis.com/maps/api/js?key=' + this.apiKey + '&libraries=places';
    } else {
      script.src = 'https://maps.googleapis.com/maps/api/js?key=';
    }
    document.head.appendChild(script);
    /*Get Current location*/
    this.geolocation.getCurrentPosition({ enableHighAccuracy: false }).then((position) => {
      this.sourceLocation.lat = position.coords.latitude;
      this.sourceLocation.lng = position.coords.longitude;
    });
    /*Map options*/
    this.mapOptions = {
      center: this.sourceLocation,
      zoom: 15,
      mapTypeControl: false
    };

    setTimeout(() => {
      this.map = new google.maps.Map(this.mapElement.nativeElement, this.mapOptions);
      this.setSourceAddress();
    }, 5000);
  }
  @ViewChild('Map') mapElement: ElementRef;
  map: any;
  mapOptions: any;
  sourceLocation = { lat: null, lng: null, name: null };
  destinationLocation = { lat: null, lng: null, name: null};
  destinationmarkerOptions: any = { position: null, map: null, title: null };
  sourecmarkerOptions: any = { position: null, map: null, title: null };
  marker: any;
  apiKey: any = 'AIzaSyCtPkZ9pSU34VXWGihx_i4Ca4HgL4puVJ0'; /*Your API Key*/ // 'AIzaSyCtPkZ9pSU34VXWGihx_i4Ca4HgL4puVJ0'
  user: any;
  userReady: Boolean = false;
  sourceLoc: any;
  destinationLoc: any;

  autocompleteService: any;
  placesService: any;
  query: String = '';
  queryForSource: String = '';
  destiPlaces: any = [];
  sourcePlaces: any = [];
  searchDisabled: boolean;
  saveDisabled: boolean;
  selectLocation: any;
  icons = {};
  fareAmount: number;


  // Method to get search places and form autocomplete list
  searchPlace(val) {
    let config;
    this.saveDisabled = true;
    if (this.query.length > 0 || this.sourceLocation.name.length > 0) {
    if (val === 'source') {
         config = {
          types: ['geocode'],
          input: this.sourceLocation.name
        };
        this.autocompleteService.getPlacePredictions(config, (predictions, status) => {
          if (status === google.maps.places.PlacesServiceStatus.OK && predictions) {
            this.sourcePlaces = [];
            predictions.forEach((prediction) => {
              this.sourcePlaces.push(prediction);
            });
          }
        });
    } else {
         config = {
          types: ['geocode'],
          input: this.query
        };
        this.autocompleteService.getPlacePredictions(config, (predictions, status) => {
          if (status === google.maps.places.PlacesServiceStatus.OK && predictions) {
            this.destiPlaces = [];
            predictions.forEach((prediction) => {
              this.destiPlaces.push(prediction);
            });
          }
        });
    }

    } else {
      this.destiPlaces = []; this.sourcePlaces = [];
      this.sourceLocation.name = '';
      this.destinationLocation.name = '';
    }
  }

  // Method to select specific place from the list and  get place details.
  selectPlace(place, val) {
    this.sourcePlaces = [];
    this.destiPlaces = [];
    const selectLocation = {
      lat: null,
      lng: null,
      name: place.name
    };
    if (val === 'source') {
      this.sourceLocation.name = place.description;
      this.placesService.getDetails({ placeId: place.place_id }, (details) => {
        this.zone.run(() => {
          selectLocation.name = details.name;
          selectLocation.lat = details.geometry.location.lat();
          selectLocation.lng = details.geometry.location.lng();
          this.saveDisabled = false;
          this.sourceLocation = selectLocation;
          if (this.destinationLocation) {
            this.getDirections();
          }
        });
      });
    } else {
      this.query = place.description;
      this.placesService.getDetails({ placeId: place.place_id }, (details) => {
        this.zone.run(() => {
          selectLocation.name = details.name;
          selectLocation.lat = details.geometry.location.lat();
          selectLocation.lng = details.geometry.location.lng();
          this.saveDisabled = false;
          this.destinationLocation = selectLocation;
          if (this.sourceLocation) {
            this.getDirections();
          }
        });
      });
    }
  }

  // Method to route rendered on the map
  getDirections() {
    const directionsService = new google.maps.DirectionsService;
    const directionsDisplay = new google.maps.DirectionsRenderer;
    this.map = new google.maps.Map(this.mapElement.nativeElement, this.mapOptions);
    directionsDisplay.setMap(this.map);
    this.calculateAndDisplayRoute(directionsService, directionsDisplay);
  }

  // Utility method to convert object of lat and lng to single comma separated string to be sent to the google api
  convertObjectToString(val) {
    let str = '';
    str = val.lat + ', ' + val.lng;
  return str;
  }

  // Display route on the map based on the hotspots receive from the backend
  calculateAndDisplayRoute(directionsService, directionsDisplay) {
    const waypts = [];
    waypts.push({
      location: '12.977921, 77.714472',
      stopover: true
    });
    waypts.push({
      location: '12.977638, 77.709937',
      stopover: true
    });
    const lineSymbol = {
      path: 'M 0,-1 0,1',
      strokeOpacity: 1,
      scale: 4
    };
    directionsService.route({
      origin: this.convertObjectToString(this.sourceLocation),
      destination: this.convertObjectToString(this.destinationLocation),
      waypoints: waypts,
      optimizeWaypoints: true,
      travelMode: 'WALKING'
    }, function (response, status) {
      if (status === 'OK') {
        directionsDisplay.setDirections(response);
        directionsDisplay.setOptions({
          polylineOptions: {
                      // strokeWeight: 4,
                      strokeOpacity: 0,
                      strokeColor:  'green',
                      icons: [{
                        icon: lineSymbol,
                        offset: '0',
                        repeat: '20px'
                      }]
                  }
          });
      } else {
        window.alert('Directions request failed due to ' + status);
      }
    });
  }

  // Method is called to redirect the page to home page.
  public onClickCancel() {
    this.router.navigate(['/tabs/tab1']);
  }

  // Method to call google address api to the fetch the address 
  getAddress(lat, lng) {
    return new Promise(function (resolve, reject) {
      const request = new XMLHttpRequest();

      const method = 'GET';
      const url = 'https://maps.googleapis.com/maps/api/geocode/json?latlng=' + lat + ',' + lng +
        '&sensor=true&key=AIzaSyCtPkZ9pSU34VXWGihx_i4Ca4HgL4puVJ0';
      const async = true;

      request.open(method, url, async);
      request.onreadystatechange = function () {
        if (request.readyState === 4) {
          if (request.status === 200) {
            const data = JSON.parse(request.responseText);
            const address = data.results[0].formatted_address;
            resolve(address);
          } else {
            reject(request.status);
          }
        }
      };
      request.send();
    });
  }
  // Method to set the address for source fetched from gps to the source location box.
  async setSourceAddress() {
    // await code here
    const result = await this.getAddress(this.sourceLocation.lat, this.sourceLocation.lng);
    // code below here will only execute when await makeRequest() finished loading
    this.sourceLocation.name = result;
    this.autocompleteService = new google.maps.places.AutocompleteService();
    this.placesService = new google.maps.places.PlacesService(this.map);
    // this.searchDisabled = false;
  }

  bookRide() {

  }

  confirmRide() {

  }

  joinRide() {

  }

}
