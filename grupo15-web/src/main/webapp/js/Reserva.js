
var res = new Vue({
    el: "#res",
    data: () => ({
      fecha:'',
      IdEnf:'',
      IdVac:'',
      Hora:'',
      IdPlan:'',
      listaEnfermedades: [],
      listaVacunatorios: [],
      listaPlanes: [],
      listaHoras: [],

    }),

    mounted(){
    
      
      this.CargarEnfermedades();
     
    },

    methods: 
    {
      
      SetDatos () {
      IdEnf=this.IdEnf;
      console.log("IdEnf:" + this.IdEnf);
      IdPlan=this.IdPlan;
      console.log("IdPlan:" + this.IdPlan);
      IdVac= this.IdVac;
      console.log("IdVac:" + this.IdVac);
      fecha=this.fecha;
      console.log("Fecha:" + this.fecha);
      Hora=this.Hora;
      console.log("Hora:" + this.Hora);
      const reserva = {
        idEnfermedad : this.IdEnf.toString(),
        idPlan : this.IdPlan.toString(),
        idVacunatorio : this.IdVac.toString(),
        fecha : this.fecha.toString(),
        hora : this.Hora.toString(),
      };
      
      axios.post("http://localhost:8080/grupo15-services/rest/reservas/confirmar", {
      idEnfermedad : this.IdEnf.toString(),
      idPlan : this.IdPlan.toString(),
      idVacunatorio : this.IdVac.toString(),
      fecha : this.fecha.toString(),
      hora : this.Hora.toString(),
    }, {headers : {token}});
    console.log(this.reserva);

      },

       CargarEnfermedades () {
        
        axios.get("http://localhost:8080/grupo15-services/rest/reservas/enf/")
        .then((response => {
          console.log("GET enfermedades: ", response.data)
          this.listaEnfermedades = response.data
          }))
    
        },

        SeleccionarEnfermedades () {
          IdEnf = this.IdEnf;
          axios.get("http://localhost:8080/grupo15-services/rest/reservas/enf/" + this.IdEnf.toString())
          .then((response => {
            console.log("GET planes: ", response.data)
            this.listaPlanes = response.data
            console.log("planes: " + this.listaPlanes.nombre);
            }))
      
          },


          SeleccionarPlanes () {
        
            IdPlan = this.IdPlan;
            axios.get("http://localhost:8080/grupo15-services/rest/reservas/pv" + "?p="+ this.IdPlan.toString())
          this.CargarVacunatorios();
        
            },

        CargarVacunatorios () {
        
          axios.get("http://localhost:8080/grupo15-services/rest/reservas/vac/")
          .then((response => {
            console.log("GET vacunatorios: ", response.data)
            this.listaVacunatorios = response.data
            }))
      
          },

          SeleccionarVacunatorio () {
            IdVac = this.IdVac;
            },
          SeleccionarFecha() {
            fecha=this.fecha;
            axios.get("http://localhost:8080/grupo15-services/rest/reservas/fecha" + "?vac="+ this.IdVac.toString() +"&date="+ this.fecha.toString()  +"&p="+ this.IdPlan.toString())
              .then((response => {
                console.log("GET horas: ", response.data)
                this.listaHoras = response.data
                }))
          },

            CargarHoras () {
              Hora=this.Hora;
  
          
              }


              
    }
     
  
   })