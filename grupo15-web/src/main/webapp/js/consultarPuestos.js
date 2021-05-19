
var vac = new Vue({
  el: "#vac",
  data: {
    idVac: '',
    fecha: '',
    listaVac: [],
    vacPuesto: '',
  },
  mounted() {
    this.cargarVacunatorios();
  },


  methods: {
    setDatos() {

      idVac = this.idVac;
      console.log("idVac:" + this.idVac);
      fecha = this.fecha;
      console.log("fecha:" + this.fecha);
      this.getData();

    },
    cargarVacunatorios() {
      axios.get("http://localhost:8080/grupo15-services/rest/puestovac/vac")
        .then((response => {

          console.log(`lista Vacunatorios: `, response.data)
          this.listaVac = response.data


        }))
      },
    getData() {
   //   axios.get("http://localhost:8080/grupo15-services/rest/puestovac/asignado?user=11111111&vact=vact1&date=2021-05-12")
      axios.get("http://localhost:8080/grupo15-services/rest/puestovac/asignado?vact="+this.idVac.toString()+"&date="+this.fecha.toString())
        .then((response => {

          console.log(`VacPuesto: `, response.data)
          this.vacPuesto = response.data


        }))
   
 }

  }

})
