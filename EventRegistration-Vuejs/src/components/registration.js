import axios from 'axios'
var config = require('../../config')

/*
Vue.config.productionTip = (process.env.NODE_ENV === 'production')
function ParticipantDto (name) {
  this.name = name
  this.events = []
}

function EventDto (name, date, start, end) {
  this.name = name
  this.eventDate = date
  this.startTime = start
  this.endTime = end
}
*/

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort 

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
  name: 'eventregistration',
  data () {
    return {
      participants: [],
      events: [],
//      registrations: [],
      newParticipant: '',
      newEvent: {
        name: 'NewEvent',
        eventDate: '2017-12-08',
        startTime: '09:00',
        endTime: '11:00'
      },
      selectedParticipant: '',
      selectedEvent: '',
      errorParticipant: '',
      errorEvent: '',
      errorRegistration: '',
      response: []
    }
  },
  created: function () {
/*
// Test data
  const p1 = new ParticipantDto('Daniel');
  const p2 = new ParticipantDto('Marton');
  const e1 = new EventDto('Soccer', '2017-12-08', '09:00:00', '10:00:00');
  const e2 = new EventDto('Concert', '2017-12-09', '19:00:00', '21:00:00');
  var newEvents = [ e1, e2 ];
  var newParticipants = [ p1, p2 ];
  var newRegistrations = [ new RegisterDto(p1, e1)];
*/
    // Initializing participants from backend
    AXIOS.get('/participants')
    .then(response => {
      // JSON responses are automatically parsed.
      this.participants = response.data
    })
    .catch(e => {
      this.errorParticipant = e
      // this.errors.push(e)
    })
    // Initializing events
    AXIOS.get('/events')
    .then(response => {
      this.events = response.data
    })
    .catch(e => {
      this.errorEvent = e
      // this.errors.push(e)
    })
  },
  methods: {
//    updateEvent: function (eventName) {
//      console.log('Update Button Clicked: ' + eventName)
//    },
    createParticipant: function (participantName) {
      AXIOS.post('/participants/'.concat(participantName), {}, {
//        params: {participantName: participantName}
          
      })
        .then(response => {
        // JSON responses are automatically parsed.
          this.participants.push(response.data)
          this.newParticipant = ''
          this.errorParticipant = ''
        })
        .catch(e => {
          var errorMsg = e.response.data.message
          console.log(errorMsg)
          this.errorParticipant = errorMsg
        })
    },

    createEvent: function (eventName, eventDate, startTime, endTime) {
//      var nEvent = new EventDto(eventName, eventDate, startTime, endTime);
//      this.events.push(nEvent)
      AXIOS.post('/events/'.concat(eventName), {}, {
        params: {
          date: eventDate,
          startTime: startTime,
          endTime: endTime
        }})
        .then(response => {
        // JSON responses are automatically parsed.
          this.events.push(response.data)
          this.errorEvent = ''
          this.newEvent.name = 'NewEvent'
        })
        .catch(e => {
          var errorMsg = e.response.data.message
          console.log(errorMsg)
          this.errorEvent = errorMsg
        })
    },

    registerEvent: function (participantName, eventName) {
      var indexEv = this.events.map(x => x.name).indexOf(eventName)
      var indexPart = this.participants.map(x => x.name).indexOf(participantName)
      var participant = this.participants[indexPart]
      var event = this.events[indexEv]
//      var nReg = new RegisterDto( participant, event);
      AXIOS.post('/register', {},
        {params: {
          participant: participant.name,
          event: event.name}})
      .then(response => {
        // Update appropriate DTO collections
        participant.events.push(event)
        this.selectedParticipant = ''
        this.selectedEvent = ''
        // registrations.push(nReg);
      })
      .catch(e => {
        var errorMsg = e.response.data.message
        console.log(errorMsg)
        this.errorRegistration = errorMsg
      })
    }
  }
}
