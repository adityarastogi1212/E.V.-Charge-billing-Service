package net.ev.dao;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import net.ev.model.EV;
import net.ev.utils.JDBCUtils;



public class EvDaoImpl implements EvDao {

	private static final String INSERT_Ev_SQL = "INSERT INTO todos"
			+ "  (title, username, description, service_date,  is_done) VALUES " + " (?, ?, ?, ?, ?);";

	private static final String SELECT_Ev_BY_ID = "select id,title,username,description,service_date,is_done from todos where id =?";
	private static final String SELECT_ALL_Evs = "select * from evs";
	private static final String DELETE_Ev_BY_ID = "delete from todos where vehicle_no = ?;";
	private static final String UPDATE_Ev = "update evs set title = ?, username= ?, description =?, service_date =?, is_done = ? where vehicle_no = ?;";

	public EvDaoImpl() {
	}

	@Override
	public void insertEv(EV ev) throws SQLException {
		System.out.println(INSERT_Ev_SQL);
		// try-with-resource statement will auto close the connection.
		try (Connection connection = JDBCUtils.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_Ev_SQL)) {
			preparedStatement.setString(1, ev.getTitle());
			preparedStatement.setString(2, ev.getUsername());
			preparedStatement.setString(3, ev.getDescription());
			preparedStatement.setDate(4, JDBCUtils.getSQLDate(ev.getserviceDate()));
			preparedStatement.setBoolean(5, ev.getStatus());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			JDBCUtils.printSQLException(exception);
		}
	}

	@Override
	public EV selectEv(long evId) {
		EV ev = null;
		// Step 1: Establishing a Connection
		try (Connection connection = JDBCUtils.getConnection();
				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_Ev_BY_ID);) {
			preparedStatement.setLong(1, evId);
			System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();

			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				long vehicle_no = rs.getLong("vehicle_no");
				String title = rs.getString("title");
				String username = rs.getString("username");
				String description = rs.getString("description");
				LocalDate serviceDate = rs.getDate("service_date").toLocalDate();
				boolean isDone = rs.getBoolean("is_done");
				ev = new EV(vehicle_no, title, username, description, serviceDate, isDone);
			}
		} catch (SQLException exception) {
			JDBCUtils.printSQLException(exception);
		}
		return ev;
	}

	@Override
	public List<EV> selectAllEvs() {

		// using try-with-resources to avoid closing resources (boiler plate code)
		List<EV> evs = new ArrayList<>();

		// Step 1: Establishing a Connection
		try (Connection connection = JDBCUtils.getConnection();

				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_Evs);) {
			System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();

			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				long vehicle_no = rs.getLong("vehicle_no");
				String title = rs.getString("title");
				String username = rs.getString("username");
				String description = rs.getString("description");
				LocalDate serviceDate = rs.getDate("service_date").toLocalDate();
				boolean isDone = rs.getBoolean("is_done");
				evs.add(new EV(vehicle_no, title, username, description, serviceDate, isDone));
			}
		} catch (SQLException exception) {
			JDBCUtils.printSQLException(exception);
		}
		return evs;
	}

	@Override
	public boolean deleteEv(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = JDBCUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_Ev_BY_ID);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}


	@Override
	public boolean updateEv(EV ev) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = JDBCUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_Ev_BY_ID);) {
			statement.setString(1, ev.getTitle());
			statement.setString(2, ev.getUsername());
			statement.setString(3, ev.getDescription());
			statement.setDate(4, JDBCUtils.getSQLDate(ev.getTargetDate()));
			statement.setBoolean(5, ev.getStatus());
			statement.setLong(6, ev.getId());
			rowUpdated = statement.executeUpdate() > 0;
		}
		return rowUpdated;
	}

}
