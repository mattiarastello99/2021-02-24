package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMap;
	
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		
	}
	
	public List<Match> getMatches(){
		return dao.listAllMatches();
	}
	
	public String creaGrafo(Match m) {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<>();
		
		
		Graphs.addAllVertices(this.grafo, dao.getVertex(m.matchID, idMap));
		
		for(Adiacenza a : dao.getEdge(idMap, m.matchID)) {
			Graphs.addEdge(this.grafo, a.getP1(), a.getP2(), a.getPeso());
		}
		
		return String.format("Grafo creato con %d vertici e con %d archi", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
		
	}
	
	public String giocatoreMigliore() {
		
		
		Player migliore = null;
		Double delta = Double.MIN_VALUE;
		
		for(Player p : this.grafo.vertexSet()) {
			
			Double entranti = 0.0;
			Double uscenti = 0.0;
			
			for(DefaultWeightedEdge arco : this.grafo.outgoingEdgesOf(p)) {
				uscenti+=this.grafo.getEdgeWeight(arco);
				
			}
			for(DefaultWeightedEdge arco : this.grafo.incomingEdgesOf(p)) {
				entranti+=this.grafo.getEdgeWeight(arco);
			}
			
			if((uscenti-entranti)>delta) {
				migliore = p;
				delta = uscenti - entranti;
			}
		}
		
		String s = "\nGiocatore migliore: " + migliore + " con delta: " + delta + "\n";
		
		
		
		return s;
	}
	
}
