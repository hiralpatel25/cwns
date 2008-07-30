package gov.epa.owm.mtb.cwns.dao;

import java.util.Collection;

public interface PopulationDAO extends DAO {
	public Collection getPopulationComments(String facNum);
}
