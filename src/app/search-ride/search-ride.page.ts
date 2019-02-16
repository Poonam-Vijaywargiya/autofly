import { Component, ElementRef, NgZone, ViewChild } from '@angular/core';
import { Geolocation } from '@ionic-native/geolocation/ngx';
import { Router, ActivatedRoute } from '@angular/router';
import { NativeStorage } from '@ionic-native/native-storage/ngx';
import { GooglePlus } from '@ionic-native/google-plus/ngx';
import { LoadingController, ToastController } from '@ionic/angular';

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
    private router: Router,
    private route: ActivatedRoute,
    public toastCtrl: ToastController) {
    /*load google map script dynamically */
    const script = document.createElement('script');

    script.id = 'googleMap';
    this.userId = this.route.snapshot.paramMap.get('userId');
    this.walletBal = this.route.snapshot.paramMap.get('walletBal');
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
      this.map = new google.maps.Map(this.mapElement.nativeElement, this.mapOptions);
      this.setSourceAddress();
    });
    /*Map options*/
    this.mapOptions = {
      center: this.sourceLocation,
      zoom: 15,
      mapTypeControl: false
    };
  }
  @ViewChild('Map') mapElement: ElementRef;
  map: any;
  mapOptions: any;
  sourceLocation = { lat: null, lng: null, name: null };
  destinationLocation = { lat: null, lng: null, name: null };
  apiKey: any = 'AIzaSyCtPkZ9pSU34VXWGihx_i4Ca4HgL4puVJ0'; /*Your API Key*/ // 'AIzaSyCtPkZ9pSU34VXWGihx_i4Ca4HgL4puVJ0'
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
  fareToShow: any;
  isenabled: Boolean = false;
  isRideStarted: Boolean = false;
  walletBal: any;
  userId: any;
  hotSpots: any;
  autoNumber: any;
  addMoney: Boolean = false;
  tripId: any;
  rideId: any;
  zoneId: any;
  showBookRide: Boolean = true;
  showConfirmRide: Boolean = false;
  showIHaveBoarded: Boolean = false;
  showReachedLocation: Boolean = false;
  lastStopreached: Boolean = true;
  buttonColor: any;
  countForButtons = 0;
  nameOnButton: any;
  fromHotspotId: any;
  toHotspotId: any;
  // Method to get search places and form autocomplete list
  searchPlace(val) {
    let config;
    this.saveDisabled = true;
    if (this.query.length > 0 || (this.sourceLocation && this.sourceLocation.name.length > 0)) {
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
      } else if (this.query) {
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
      this.sourceLocation = { lat: null, lng: null, name: null };
      this.destinationLocation = { lat: null, lng: null, name: null };
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
        });
      });
    }
  }

  // Method to route rendered on the map
  getDirections() {
    const directionsService = new google.maps.DirectionsService;
    this.map = new google.maps.Map(this.mapElement.nativeElement, this.mapOptions);
    const directionsDisplayW1 = new google.maps.DirectionsRenderer({
      suppressMarkers: true,
      map: this.map,
      preserveViewport: true
    });
    const directionsDisplayW2 = new google.maps.DirectionsRenderer({
      suppressMarkers: true,
      map: this.map,
      preserveViewport: true
    });
    const directionsDisplayD = new google.maps.DirectionsRenderer;
    directionsDisplayD.setMap(this.map);
    this.calculateAndDisplayRoute(directionsService, directionsDisplayD, directionsDisplayW1, directionsDisplayW2);
  }

  // Utility method to convert object of lat and lng to single comma separated string to be sent to the google api
  convertObjectToString(val) {
    let str = '';
    str = val.lat + ', ' + val.lng;
    return str;
  }

  async presentLoading(loading) {
    return await loading.present();
  }

  // Display route on the map based on the hotspots receive from the backend
  async calculateAndDisplayRoute(directionsService, directionsDisplayD,  directionsDisplayW1, directionsDisplayW2, ) {
    const waypts = [];
    const loading = await this.loadingController.create({
      message: 'Please wait...'
    });
    this.presentLoading(loading);
    this.fareAmount = this.hotSpots.fare; // response returned from service
    this.fareToShow = 'Rs. ' + this.fareAmount;
    const lineSymbol = {
      path: google.maps.SymbolPath.CIRCLE,
      strokeOpacity: 1,
      scale: 2
    };
    // check if source to first hotspot you need to walk or not
    if (this.hotSpots.walkFromSource && (this.hotSpots.walkFromSource[0].lng !== this.hotSpots.walkFromSource[1].lng)
     && (this.hotSpots.walkFromSource[0].lat !== this.hotSpots.walkFromSource[1].lat)) {
      await directionsService.route({
        origin: this.convertObjectToString(this.hotSpots.walkFromSource[0]),
        destination: this.convertObjectToString(this.hotSpots.walkFromSource[1]),
        waypoints: [],
        optimizeWaypoints: true,
        travelMode: 'WALKING'
      }, function (response, status) {
        if (status === 'OK') {
          directionsDisplayW1.setDirections(response);
          directionsDisplayW1.setOptions({
            polylineOptions: {
              strokeWeight: 4,
              strokeOpacity: 0,
              strokeColor: 'green',
              icons: [{
                icon: lineSymbol,
                offset: '0',
                repeat: '20px'
              }]
            }
          });
        } else {
          window.alert('Directions request failed due to ' + status);
        } loading.dismiss();
      });
    }
    // check from last spot do you need to walk to the destination or not
    if (this.hotSpots.walkToDestination && (this.hotSpots.walkToDestination[0].lng !== this.hotSpots.walkToDestination[1].lng)
    && (this.hotSpots.walkToDestination[0].lat !== this.hotSpots.walkToDestination[1].lat)) {
      await directionsService.route({
        origin: this.convertObjectToString(this.hotSpots.walkToDestination[0]),
        destination: this.convertObjectToString(this.hotSpots.walkToDestination[1]),
        waypoints: [],
        optimizeWaypoints: true,
        travelMode: 'WALKING'
      }, function (response, status) {
        if (status === 'OK') {
          directionsDisplayW2.setDirections(response);
          directionsDisplayW2.setOptions({
            polylineOptions: {
              strokeWeight: 4,
              strokeOpacity: 0,
              strokeColor: 'red',
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

       // mark the hotspots in the map
    if (this.hotSpots.route) {
      this.hotSpots.route.forEach((element, i) => {
        if (i > 0 && i < this.hotSpots.route.length - 1) {
          waypts.push({
            location: this.convertObjectToString(element),
            stopover: true
          });
        }
      });
      await directionsService.route({
        origin: this.convertObjectToString(this.hotSpots.route[0]),
        destination: this.convertObjectToString(this.hotSpots.route[this.hotSpots.route.length - 1]),
        waypoints: waypts,
        optimizeWaypoints: true,
        travelMode: 'DRIVING'
      }, function (response, status) {
        if (status === 'OK') {
          directionsDisplayD.setDirections(response);
        } else {
          window.alert('Directions request failed due to ' + status);
        }
        loading.dismiss();
      });
    }
  }

  // Method is called to redirect the page to home page.
  public onClickCancel() {
    this.router.navigate(['/tabs/tab1']);
  }

  // Method to call google address api to the fetch the address
  getAddress(lat, lng) {
    const method = 'GET';
    const url = 'https://maps.googleapis.com/maps/api/geocode/json?latlng=' + lat + ',' + lng +
    '&sensor=true&key=AIzaSyCtPkZ9pSU34VXWGihx_i4Ca4HgL4puVJ0';
    return this.commonAPICall(url, null,  method);
  }
  // Method to set the address for source fetched from gps to the source location box.
  async setSourceAddress() {
    // await code here
    const result = await this.getAddress(this.sourceLocation.lat, this.sourceLocation.lng);
    // code below here will only execute when await makeRequest() finished loading
    this.sourceLocation.name = result['results'][0].formatted_address;
    this.autocompleteService = new google.maps.places.AutocompleteService();
    this.placesService = new google.maps.places.PlacesService(this.map);
  }

  async bookRide() {
    if (this.sourceLocation.name && this.destinationLocation.name) {
       this.hotSpots = await this.getHotSpots();
      this.buttonsHideAndShow(false, true, false, false);
      this.getDirections();
    } else {
      this.presentToast('Please enter source and destination locations to book ride');
    }
  }

  // Method which gives the start, end and way points and fare details
  getHotSpots() {
    const currentTime = new Date().toISOString();
    const requestParam = {
      'source': this.sourceLocation,
      'destination': this.destinationLocation,
      'passengerId': this.userId,
      'departureTime': currentTime
    };
    const method = 'POST';
    const url = 'http://autofly.us-east-2.elasticbeanstalk.com/autofly/getRoute';
    return this.commonAPICall(url, requestParam,  method);
  }

  async confirmRide() {
    if (this.walletBal < this.fareAmount) {
      this.presentToast('Sorry! you have insufficient balance. Please add balance');
      this.addMoney = true;
    } else {
      const autoDetails = await this.confirmTrip();
      this.autoNumber =  'Auto No. ' + autoDetails['driver'].autoVehicleNo;
      this.buttonsHideAndShow(false, false, true, false);
      this.tripId = autoDetails['passengerTripId'];
      this.rideId = autoDetails['ride'].rideId;
      this.nameOnButton = this.hotSpots.route[this.countForButtons].name;
    }
  }

  confirmTrip() {
    const requestParam = this.hotSpots;
    const method = 'POST';
    const url = 'http://autofly.us-east-2.elasticbeanstalk.com/autofly/confirmTrip';
    return this.commonAPICall(url, requestParam,  method);
  }

  async joinedRide() {
   const rideJoined = await this.rideJoined();
   if (rideJoined['success']) {
    this.buttonsHideAndShow(false, false, false, true);
      if (this.countForButtons < this.hotSpots.route.length - 2) {
        this.countForButtons += 1;
        this.nameOnButton = this.hotSpots.route[this.countForButtons].name;
        this.zoneId = this.hotSpots.route[this.countForButtons].currentZoneId;
        this.fromHotspotId = this.hotSpots.route[this.countForButtons].id;
        this.toHotspotId = this.hotSpots.route[this.countForButtons + 1].id;
        this.lastStopreached = false;
        this.buttonColor = 'warning';
      } else if (this.countForButtons < this.hotSpots.route.length - 1) {
          this.countForButtons += 1;
          this.nameOnButton = this.hotSpots.route[this.countForButtons].name;
          this.buttonColor = 'success';
          this.lastStopreached = true;
      }
    } else {
      this.presentToast('Some internal error occured, please try again.');
    }
  }

  rideJoined() {
    const requestParam = {
      'passengerId': this.userId,
      'rideId': this.rideId,
      'passengerTripId':  this.tripId,
    };
    const method = 'POST';
    const url = 'http://autofly.us-east-2.elasticbeanstalk.com/autofly/addPassenger';
    return this.commonAPICall(url, requestParam,  method);
  }

  async reachedLocation() {
    if (!this.lastStopreached) {
      const result = await this.findAutoDetails();
      if (result['foundAuto']) {
        this.buttonsHideAndShow(false, false, true, false);
        this.autoNumber =  'Auto No. ' + result['driver'].autoVehicleNo;
      } else {
        this.presentToast('Some internal error occured, please try again.');
      }
    } else {
      const msg = 'Thanks for riding with us, ' + this.fareToShow + ' has been deducted from your wallet.';
      this.presentToast(msg);
      this.buttonsHideAndShow(true, false, false, false);
      this.countForButtons = 0;
      const result = await this.endRideForHotspot();
      this.map = new google.maps.Map(this.mapElement.nativeElement, this.mapOptions);
    }
  }

  endRideForHotspot() {
    const requestParam = {
      'tripId': this.tripId,
    };
    const method = 'POST';
    const url = 'http://autofly.us-east-2.elasticbeanstalk.com/autofly/endTrip';
    return this.commonAPICall(url, requestParam,  method);
  }

  findAutoDetails() {
    const requestParam = {
      'zoneId': this.zoneId,
      'passengerId': this.userId,
      'fromHotspotId': this.fromHotspotId,
      'toHotspotId': this.toHotspotId,
    };
    const method = 'POST';
    const url = 'http://autofly.us-east-2.elasticbeanstalk.com/autofly/findAuto';
    return this.commonAPICall(url, requestParam,  method);
  }

  buttonsHideAndShow(book, confirm, boarded, reached) {
    this.showBookRide = book;
    this.showConfirmRide = confirm;
    this.showIHaveBoarded = boarded;
    this.showReachedLocation = reached;
  }
   // common method to call api
   commonAPICall(url, requestParams, methodType) {
    return new Promise(function (resolve, reject) {
      const request = new XMLHttpRequest();
      const async = true;
      request.open(methodType, url, async);
      request.onreadystatechange = function () {
        if (request.readyState === 4) {
          if (request.status === 200) {
            const response = JSON.parse(request.responseText);
            resolve(response);
          } else {
            reject(request.status);
          }
        }
      };
      if (requestParams) {
        request.setRequestHeader('Content-Type', 'application/json');
        request.send(JSON.stringify(requestParams));
      } else {
        request.send();
      }
    });
  }

  async presentToast(msg) {
    const toast = await this.toastCtrl.create({
      message: msg,
      duration: 2000
    });
    toast.present();
  }

  cancleRide() {
    this.presentToast('You have cancled the ride, please click on confirm to confirm the booking');
  }

}
