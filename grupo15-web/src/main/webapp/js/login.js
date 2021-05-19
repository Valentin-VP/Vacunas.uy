
var vac = new Vue({
  el: "#vac",
  data: () => ({
    token: "",
  }),

  mounted() {
    var url = window.location;
    this.token = new URLSearchParams(url.search).get('x-access-token');
   // this.token = token;
   console.log("token:", this.token);
   console.log("store", getToken());
   setToken(this.token);
   console.log("store", getToken());
  },

  
})