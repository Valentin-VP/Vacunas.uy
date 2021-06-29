var res = new Vue({
	el: "#res",
	data: {

		listaReservas: [],
		reserva: '',
		selectedIndex: ''
	},
	mounted() {
		this.getReservas();
	},




	methods:
	{
		aEliminar() {

			reserva = this.reserva;
			console.log('lista Eliminar: ', this.reserva);
			this.eliminarDatos();

		},
		updateIndex(event, selectedIndex) {
			console.log(event, selectedIndex);
			console.log("/grupo15-services/rest/reservas/eliminar?e=" + this.listaReservas[selectedIndex - 1].enfermedad.toString() + "&date=" + this.listaReservas[selectedIndex - 1].fecha.toString());
			this.selectedIndex = selectedIndex;
		},
		eliminarDatos() {


			if (this.listaReservas[this.selectedIndex - 1]){
				axios.delete("/grupo15-services/rest/reservas/eliminar?e=" + this.listaReservas[this.selectedIndex - 1].enfermedad.toString() + "&date=" + this.listaReservas[this.selectedIndex - 1].fecha.toString()).then(response => {
				
					this.getReservas();
				})
			}
			
		},

		getReservas() {


			axios.get("/grupo15-services/rest/reservas/listar")
				.then(response => {

					console.log('lista Reservas: ', response.data)
					this.listaReservas = response.data;


				}).catch(error=>{
					console.log(error.response)})
					this.listaReservas = []

		},



	},









})