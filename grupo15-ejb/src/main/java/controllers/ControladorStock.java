package controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtHistoricoStock;
import datatypes.DtStock;
import entities.Historico;
import entities.Stock;
import entities.Vacuna;
import entities.Vacunatorio;
import exceptions.CantidadNula;
import exceptions.StockVacunaVacunatorioExistente;
import exceptions.StockVacunaVacunatorioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IStockDaoLocal;
import interfaces.IStockDaoRemote;
import persistence.StockID;

@Stateless
public class ControladorStock implements IStockDaoLocal, IStockDaoRemote {

	@PersistenceContext(name = "test")
	private EntityManager em;

	public ControladorStock() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void agregarStock(String idVacunatorio, String idVacuna, Integer cantidad)
			throws VacunatorioNoCargadoException, VacunaInexistente, CantidadNula, StockVacunaVacunatorioExistente {
		// PRE: Debe existir el vacunatorio
		// Debe existir la vacuna
		// cantidad deb ser mayor a 0
		// No debe existir un Stock de esa vacuna para ese vacunatorio
		if (cantidad <= 0) {
			throw new CantidadNula("La cantidad debe ser mayor a 0");
		}
		Vacunatorio vacunatorio = em.find(Vacunatorio.class, idVacunatorio);
		if (vacunatorio == null) {
			throw new VacunatorioNoCargadoException("No existe el vacunatorio con ID " + idVacunatorio);
		}
		Vacuna vacuna = em.find(Vacuna.class, idVacuna);
		if (vacuna == null) {
			throw new VacunaInexistente("No existe la vacuna con ID " + idVacuna);
		}
		for (Stock vacunatorioStock : vacunatorio.getStock()) {
			if (vacunatorioStock.getVacuna().getNombre().equals(idVacuna)) {
				throw new StockVacunaVacunatorioExistente(
						String.format("Ya existe Stock de la Vacuna %s en el Vacunatorio %s", idVacuna, idVacunatorio));
			}
		}
		Stock stock = new Stock(vacunatorio, vacuna, cantidad, 0, 0, 0);
		vacunatorio.getStock().add(stock);
		em.persist(vacunatorio);
	}

	@Override
	public void modificarStock(String idVacunatorio, String idVacuna, Integer cantidad, Integer descartadas,
			Integer administradas, Integer disponibles)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		Vacunatorio vacunatorio = em.find(Vacunatorio.class, idVacunatorio);
		if (vacunatorio == null) {
			throw new VacunatorioNoCargadoException("No existe el vacunatorio con ID " + idVacunatorio);
		}
		Vacuna vacuna = em.find(Vacuna.class, idVacuna);
		if (vacuna == null) {
			throw new VacunaInexistente("No existe la vacuna con ID " + idVacuna);
		}
		for (Stock stock : vacunatorio.getStock()) {
			if (stock.getVacuna().getNombre().equals(idVacuna)) {
				stock.setCantidad(cantidad);
				stock.setDescartadas(descartadas);
				stock.setAdministradas(administradas);
				stock.setDisponibles(disponibles);
				em.merge(stock);
				return;
			}
		}
		// Si ejecuta aqui, significa que no encontro el Stock
		throw new StockVacunaVacunatorioInexistente(
				String.format("No se encontro Stock de la Vacuna %s en el Vacunatorio %s", idVacuna, idVacunatorio));

	}

	@Override
	public void eliminarStock(String idVacunatorio, String idVacuna)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		Vacunatorio vacunatorio = em.find(Vacunatorio.class, idVacunatorio);
		if (vacunatorio == null) {
			throw new VacunatorioNoCargadoException("No existe el vacunatorio con ID " + idVacunatorio);
		}
		Vacuna vacuna = em.find(Vacuna.class, idVacuna);
		if (vacuna == null) {
			throw new VacunaInexistente("No existe la vacuna con ID " + idVacuna);
		}
		for (Stock stock : vacunatorio.getStock()) {
			if (stock.getVacuna().getNombre().equals(idVacuna)) {
				em.remove(stock);
				return;
			}
		}
		throw new StockVacunaVacunatorioInexistente(
				String.format("No se encontro Stock de la Vacuna %s en el Vacunatorio %s", idVacuna, idVacunatorio));
	}

	@Override
	public DtStock obtenerStock(String idVacunatorio, String idVacuna)
			throws VacunatorioNoCargadoException, VacunaInexistente, StockVacunaVacunatorioInexistente {
		DtStock dtStock = null;
		Stock stock = em.find(Stock.class, new StockID(idVacunatorio, idVacuna));
		if (stock != null) {
			dtStock = new DtStock(stock.getVacunatorio().getNombre(), stock.getVacuna().getNombre(),
					stock.getCantidad(), stock.getDescartadas(), stock.getDisponibles(), stock.getAdministradas());
			if (stock.getHistoricos().size() > 0) {
				for (Historico historico: stock.getHistoricos()) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
					String strDate = historico.getFecha().format(formatter);
					DtHistoricoStock dtHistorico = new DtHistoricoStock(strDate, historico.getCantidad(),
							historico.getDescartadas(), historico.getDisponibles(), historico.getAdministradas(), 
							historico.getStock().getVacuna().getNombre(), historico.getStock().getVacunatorio().getId());
					dtStock.getHistoricos().add(dtHistorico);
				}
			}
		} else {
			throw new StockVacunaVacunatorioInexistente(String.format("No se encontro Stock de la Vacuna %s en el Vacunatorio %s", idVacuna, idVacunatorio));
		}
		return dtStock;
	}

	@Override
	public List<DtStock> listarStock(String idVacunatorio) throws VacunatorioNoCargadoException {
		Vacunatorio vacunatorio = em.find(Vacunatorio.class, idVacunatorio);
		List<DtStock> stockVacunatorio = new ArrayList<DtStock>();
		if (vacunatorio == null) {
			throw new VacunatorioNoCargadoException("No existe el vacunatorio con ID " + idVacunatorio);
		}
		for (Stock stock : vacunatorio.getStock()) {
			DtStock dtStock = new DtStock(stock.getVacunatorio().getNombre(), stock.getVacuna().getNombre(),
					stock.getCantidad(), stock.getDescartadas(), stock.getDisponibles(), stock.getAdministradas());
			if (stock.getHistoricos().size() > 0) {
				for (Historico historico: stock.getHistoricos()) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
					String strDate = historico.getFecha().format(formatter);
					DtHistoricoStock dtHistorico = new DtHistoricoStock(strDate, historico.getCantidad(),
							historico.getDescartadas(), historico.getDisponibles(), historico.getAdministradas(), 
							historico.getStock().getVacuna().getNombre(), historico.getStock().getVacunatorio().getId());
					dtStock.getHistoricos().add(dtHistorico);
				}
			}
			stockVacunatorio.add(dtStock);
		}
		return stockVacunatorio;
	}

	private String prepararQueryStockGlobalActual(String enfermedad, String vacuna, String vacunatorio){
		String base = "SELECT s FROM Stock s";
		if (vacuna!=null && vacuna.equals("") == false) {
			base = base + (" WHERE vacuna = '" + vacuna + "'");
			System.out.println(base);
			if (vacunatorio!=null && vacunatorio.equals("") == false) {
				base = base + (" AND vacunatorio = '" + vacunatorio + "'");
				System.out.println(base);
			}
		}else {
			if (enfermedad!=null && enfermedad.equals("") == false) {
				System.out.println("SELECT v FROM Vacuna v WHERE enfermedad_nombre = '"+enfermedad + "'");
				Query query = em.createQuery("SELECT v FROM Vacuna v WHERE enfermedad_nombre = '"+enfermedad + "'");
				@SuppressWarnings("unchecked")
				List<Vacuna> vacunas = (List<Vacuna>) query.getResultList();
				int size = 0;
				if (!vacunas.isEmpty()) {
					base = base + (" WHERE (");
					System.out.println(base);
					size = vacunas.size();
				}
				int i = 0;
				for (Vacuna v: vacunas) {
					i++;
					base = base + ("vacuna = '" + v.getNombre() + "'");
					System.out.println(base);
					if (i<size) {
						base = base + (" OR ");
						System.out.println(base);
					}else {
						base = base + (")");
					}
				}
				if (vacunatorio!=null && vacunatorio.equals("") == false) {
					base = base + (" AND vacunatorio = '" + vacunatorio + "'");
					System.out.println(base);
				}
			}else {
				if (vacunatorio!=null && vacunatorio.equals("") == false) {
					base = base + (" WHERE vacunatorio = '" + vacunatorio + "'");
					System.out.println(base);
				}
			}
		}
		System.out.println("RESULTADO: " + base);
		return base;
	}
	
	public List<DtStock> getStockActual(String enfermedad, String vacuna, String vacunatorio) {
		List<DtStock> retorno = new ArrayList<>();
		Query query;
		query = em.createQuery(prepararQueryStockGlobalActual(enfermedad, vacuna, vacunatorio));

		@SuppressWarnings("unchecked")
		List<Stock> stocks = query.getResultList();
		if (!stocks.isEmpty()) {
			for (Stock s: stocks) {
				DtStock dt = new DtStock(s.getVacunatorio().getId(), s.getVacuna().getNombre(), s.getCantidad(), s.getDescartadas(), s.getDisponibles(), s.getAdministradas());
				dt.setHistoricos(new ArrayList<DtHistoricoStock>());
				retorno.add(dt);
			}
		}
		return retorno;
	}
	
	public Map<String, Map<String, String>> getHistoricoStock(String enfermedad, String vacuna, String vacunatorio, LocalDate fechaInicio, LocalDate fechaFin){
		Map<String, Map<String, String>> retorno = new HashMap<String, Map<String, String>>();
		Map<String, String> valores;
		Query query;
		query = em.createQuery(prepararQueryStockGlobalActual(enfermedad, vacuna, vacunatorio));

		@SuppressWarnings("unchecked")
		List<Stock> stocks = query.getResultList();
		if (!stocks.isEmpty()) {
			int barra = 0;
			for (Stock s: stocks) {
				List <Historico> historicos = s.getHistoricos();
		        for (Historico h: historicos){
		        	if (!h.getFecha().isBefore(fechaInicio) && !h.getFecha().isAfter(fechaFin)) {
		        		valores = new HashMap<String, String>();
		        		barra = h.getFecha().getMonthValue();
		        		if (retorno.get(String.valueOf(barra)) == null) {
		        			valores.put("cantTotal", String.valueOf(h.getCantidad()));
		        			valores.put("cantAdmin", String.valueOf(h.getAdministradas()));
		        			valores.put("cantDesc", String.valueOf(h.getDescartadas()));
		        			valores.put("cantDisp", String.valueOf(h.getDisponibles()));
		        		}else {
		        			valores.put("cantTotal", String.valueOf(Integer.parseInt(retorno.get(String.valueOf(barra)).get("cantTotal")) + h.getCantidad()));
		        			valores.put("cantAdmin", String.valueOf(Integer.parseInt(retorno.get(String.valueOf(barra)).get("cantAdmin")) + h.getAdministradas()));
							valores.put("cantDesc", String.valueOf(Integer.parseInt(retorno.get(String.valueOf(barra)).get("cantDesc")) + h.getDescartadas()));
							valores.put("cantDisp", String.valueOf(Integer.parseInt(retorno.get(String.valueOf(barra)).get("cantDisp")) +  h.getDisponibles()));
		        		}
						retorno.put(String.valueOf(barra), valores);
		        	}
		        }
			}
		}else {
			//valores = new HashMap<String, String>();
			//valores.put("cantTotal", String.valueOf(0));
			//valores.put("cantAdmin", String.valueOf(0));
			//valores.put("cantDesc", String.valueOf(0));
			//valores.put("cantDisp", String.valueOf(0));
			//retorno.put("0", valores);

		}
		return retorno;
		
	}
}
