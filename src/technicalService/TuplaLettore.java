package technicalService;

import java.sql.SQLException;

public class TuplaLettore extends Tupla{
	public TuplaLettore(String query) {
		super(query);
	}

	public TuplaLettore getFollows(String utenteCorrente) {
		
		String query = "SELECT id_facebook, nome, url_foto,numFollows, numFollower "
				+ "FROM utente ,segue "
				+ "WHERE utente_follower=\""+utenteCorrente+"\" and utente_follow = id_facebook;";		
		return new TuplaLettore(query);
	}
	
	public TuplaLettore getFollower(String utenteCorrente){
		
		String query = "SELECT id_facebook, nome, url_foto,numFollows, numFollower "
				+ "FROM utente ,segue "
				+ "WHERE utente_follow=\""+utenteCorrente+"\" and utente_follower = id_facebook;";
				
		return new TuplaLettore(query);
	}
	
	public TuplaFumetto getPreferiti(String utenteCorrente) {
		String query =" SELECT f.nome, f.autore, f.artista,f.completa,f.occidentale,f.url_copertina_primo_volume,"
				+ "f.valutazione_media,f.numero_letture "
				+ "FROM fumetto as f , preferiti as p "
				+ "WHERE p.utente =\""+utenteCorrente+"\" and p.nome_fumetto = f.nome;";
				
		return new TuplaFumetto(query);
	}
	
	public TuplaFumetto getDaLeggere(String utenteCorrente){
	
		String query = "SELECT f.nome, f.autore, f.artista,f.completa,f.occidentale,f.url_copertina_primo_volume,"
				+ "f.valutazione_media,f.numero_letture  "
				+ "FROM fumetto as f, da_leggere as d "
				+ "WHERE d.utente =\""+utenteCorrente+"\" and d.nome_fumetto = f.nome;";
		return new TuplaFumetto(query);
	}
	
	public TuplaFumetto getCronologia(String utente){
		String query = "SELECT r.data_lettura,f.nome, f.autore, f.artista,f.completa,f.occidentale,f.url_copertina_primo_volume,"
				+ "f.valutazione_media,f.numero_letture "
				+ "FROM fumetto as f, letture_recenti as r "
				+ "WHERE r.utente =\""+utente+"\" and r.nome_fumetto = f.nome "
				+ "ORDER BY r.data_lettura DESC; ";
		
		return new TuplaFumetto(query );
	}
	
	public TuplaSegnalibro getSegnalibri(String utente)
	{
		String query = "SELECT f.nome, f.autore, f.artista,f.completa,f.occidentale,f.url_copertina_primo_volume,"
				+ "f.valutazione_media,f.numero_letture "
				+ "FROM fumetto as f, segnalibro as s "
				+ "WHERE s.utente =\""+utente+"\" and s.nome_fumetto = f.nome ";
		
		return new TuplaSegnalibro(query );
	}
	
	public String getIdFacebook() {
		
		try {
			return cursore.getString("id_facebook");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getNome(){
		
		try {
			return cursore.getString("nome");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getUrlFoto() {
		
		try {
			return cursore.getString("url_foto");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public int getNumFollows(){
		
		try {
			return cursore.getInt("numFollows");
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int getNumFollower(){
		
		try {
			return cursore.getInt("numFollower");
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static void main(String[] args) {
		String idFacebookCorrente = "1590013667";
		String nomeFumetto = "Death Note";
		String rimuoviPreferito = "DELETE FROM preferiti WHERE utente=\""+idFacebookCorrente+"\" "
				+ "and nome_fumetto=\""+nomeFumetto+"\";";
		
	}

}
