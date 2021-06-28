
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
      ok200:''

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
      
      axios.post("/grupo15-services/rest/reservas/confirmar", {
      idEnfermedad : this.IdEnf.toString(),
      idPlan : this.IdPlan.toString(),
      idVacunatorio : this.IdVac.toString(),
      fecha : this.fecha.toString(),
      hora : this.Hora.toString(),
    })
    .then(response => {
      if (response.status === 200) {
        console.log("Respuesta: " + response.status)
        this.ok200="Se realizó la reserva correctamente"
        console.log("VariableRespuesta: " + this.ok200)
      }
      if (response.status === 400) {
        console.log("Respuesta: " + response.status)
        this.ok200="No es posible realizar esta reserva"
        console.log("VariableRespuesta: " + this.ok200)
      }

    });

    console.log(this.reserva);

      },

       CargarEnfermedades () {
        
        axios.get("/grupo15-services/rest/reservas/enf/")
        .then((response => {
          console.log("GET enfermedades: ", response.data)
          this.listaEnfermedades = response.data
          }))
    
        },

        SeleccionarEnfermedades () {
          IdEnf = this.IdEnf;
          axios.get("/grupo15-services/rest/reservas/enf/" + this.IdEnf.toString())
          .then((response => {
            console.log("GET planes: ", response.data)
            this.listaPlanes = response.data
            console.log("planes: " + this.listaPlanes.nombre);
            }))
      
          },


          SeleccionarPlanes () {
        
            IdPlan = this.IdPlan;
            axios.get("/grupo15-services/rest/reservas/pv" + "?p="+ this.IdPlan.toString())
          this.CargarVacunatorios();
        
            },

        CargarVacunatorios () {
        
          axios.get("/grupo15-services/rest/reservas/vac/")
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
            axios.get("/grupo15-services/rest/reservas/fecha" + "?vac="+ this.IdVac.toString() +"&date="+ this.fecha.toString()  +"&p="+ this.IdPlan.toString())
              .then((response => {
                if (response.status === 200) {
                console.log("GET horas: ", response.data)
                this.listaHoras = response.data
              }
                }))
          },

            CargarHoras () {
              Hora=this.Hora;
  
          
              }


              
    }
     
  
   })