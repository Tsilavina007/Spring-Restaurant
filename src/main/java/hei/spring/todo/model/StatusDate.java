package hei.spring.todo.model;

import java.time.LocalDateTime;

public class StatusDate {
	private Status status;
	private LocalDateTime updateDateTime;


	public StatusDate(Status status, LocalDateTime updateDateTime) {
		this.status = status;
		this.updateDateTime = updateDateTime;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public LocalDateTime getUpdateDateTime() {
		return updateDateTime;
	}


	public void setUpdateDateTime(LocalDateTime updateDateTime) {
		this.updateDateTime = updateDateTime;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((updateDateTime == null) ? 0 : updateDateTime.hashCode());
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
		StatusDate other = (StatusDate) obj;
		if (status != other.status)
			return false;
		if (updateDateTime == null) {
			if (other.updateDateTime != null)
				return false;
		} else if (!updateDateTime.equals(other.updateDateTime))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Statusdate [status=" + status + ", updateDateTime=" + updateDateTime + "]";
	}


}
