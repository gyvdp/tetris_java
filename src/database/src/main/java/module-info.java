open module atlj.database {
  requires java.sql;
  requires org.xerial.sqlitejdbc;
  requires atlj.model;

  exports esi.acgt.atlj.database.db;
  exports esi.acgt.atlj.database.dto;
  exports esi.acgt.atlj.database.business;
  exports esi.acgt.atlj.database.exceptions;

}