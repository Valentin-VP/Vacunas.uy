var vac = new Vue({
  el: "#vac",
  data: () => ({

  }),

  mounted(){
    var url = window.location;
    var token = new URLSearchParams(url.search).get('x-access-token');
    console.log("token:", token);
  }


 })