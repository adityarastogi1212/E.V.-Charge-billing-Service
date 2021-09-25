package net.ev.dao;
import java.sql.SQLException;
import java.util.List;

import net.ev.model.EV;


public interface EvDao {
		void insertEv(EV ev) throws SQLException;

		EV selectEv(long evId);

		List<EV> selectAllEvs();

		boolean deleteEv(int vehicle_no) throws SQLException;

		boolean updateEv(EV ev) throws SQLException;



	}

