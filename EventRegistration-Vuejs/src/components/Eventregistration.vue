<template>
  <div id="eventregistration">
    <h2>Participants</h2>
      <table>
        <tr v-for="participant in participants" >
            <td>{{ participant.name }}</td>
            <td> 
              <ul>
                <li v-for="event in participant.events">
                  {{event.name}}
                </li>
              </ul>
            </td>
        </tr>
        <tr>
            <td>
                <input type="text" v-model="newParticipant" placeholder="Participant Name">
            </td>
            <td>
                <button @click="createParticipant(newParticipant)">Create Participant</button>
            </td>
        </tr>
      </table>
    <p>
      <span v-if="errorParticipant && !newParticipant" style="color:red">Error: {{errorParticipant}} </span>
    </p>
  <hr>
  <h2>Events</h2>
  <table>
    <tr>
      <th>Event Name</th>
      <th>Date</th>
      <th>Start</th> 
      <th>End</th>
      <!--<th>Edit</th>-->
    </tr>
    <tr v-for="event in events">
      <td>{{ event.name }}</td>
      <td>{{ event.eventDate }}</td>
      <td>{{ event.startTime }}</td>
      <td>{{ event.endTime }}</td>
      <!--<td>
        <button v-on:click="updateEvent(event.name)">Edit</button>
      </td>-->
    </tr>
    <tr>
      <td>
        <input type="text" v-model="newEvent.name" placeholder="Event Name">
      </td>
      <td>
        <input type="date" v-model="newEvent.eventDate" placeholder="YYYY-MM-DD">
      </td>
      <td>
        <input type="time" v-model="newEvent.startTime" placeholder="HH:mm">
      </td>
      <td>
        <input type="time" v-model="newEvent.endTime" placeholder="HH:mm">
      </td>
      <td>
        <button 
          v-on:click="createEvent(newEvent.name, newEvent.eventDate, newEvent.startTime, newEvent.endTime)">Create</button>
      </td>
    </tr>
  </table>
      <span v-if="errorEvent" style="color:red">Error: {{errorEvent}} </span>
  <hr>
  <h2>Registrations</h2>
  <label>Participant:     
    <select v-model="selectedParticipant">
      <option disabled value="">Please select one</option>
      <option v-for="participant in participants" >
        {{ participant.name }}
      </option>
    </select>
  </label>
  <label>Event:     
    <select v-model="selectedEvent">
      <option disabled value="">Please select one</option>
      <option v-for="event in events" >
        {{ event.name }}
      </option>
    </select>
  </label>
  <button v-bind:disabled="!selectedParticipant || !selectedEvent" @click="registerEvent(selectedParticipant,selectedEvent)">Register</button>
  <hr>
  <span v-if="errorRegistration" style="color:red">Error: {{errorRegistration}} </span> 
  </div>
</template>
<script src="./registration.js"></script>
<style>
#eventregistration {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background: #f2ece8;
  margin-top: 60px;
}

h1, h2 {
  font-weight: normal;
}

ul {
  list-style-type: disc;
  text-align:left;
  padding: 1;
}

li {
  margin: 1 10px;
}

a {
  color: #42b983;
}
</style>