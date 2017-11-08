package br.com.odontofacil.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.odontofacil.model.Backup;

public interface BackupRepository  extends JpaRepository <Backup, Long>{
	
	@Query("SELECT b FROM Backup b "
			+ "WHERE DATE(inicio) = CURDATE()")
	public Backup executouBackupHoje();

}
