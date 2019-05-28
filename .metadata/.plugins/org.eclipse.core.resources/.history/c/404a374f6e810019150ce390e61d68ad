package gec.scf.dummy.bank.entity;

import java.io.Serializable;

public class MasterMappingCodeDetailPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long mappingDetailID;

	private Long mappingID;

	public Long getMappingDetailID() {
		return mappingDetailID;
	}

	public void setMappingDetailID(Long mappingDetailID) {
		this.mappingDetailID = mappingDetailID;
	}

	public Long getMappingID() {
		return mappingID;
	}

	public void setMappingID(Long mappingID) {
		this.mappingID = mappingID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mappingDetailID == null) ? 0 : mappingDetailID.hashCode());
		result = prime * result + ((mappingID == null) ? 0 : mappingID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MasterMappingCodeDetailPK other = (MasterMappingCodeDetailPK) obj;
		if (mappingDetailID == null) {
			if (other.mappingDetailID != null)
				return false;
		} else if (!mappingDetailID.equals(other.mappingDetailID))
			return false;
		if (mappingID == null) {
			if (other.mappingID != null)
				return false;
		} else if (!mappingID.equals(other.mappingID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MasterMappingCodeDetailPK [mappingDetailID=" + mappingDetailID + ", mappingID=" + mappingID + "]";
	}

}
