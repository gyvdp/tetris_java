open module tetris.database {
  requires tetris.model;
  requires java.sql;
  requires org.xerial.sqlitejdbc;

  exports esi.acgt.atlj.database.db;
  exports esi.acgt.atlj.database.dto;
  exports esi.acgt.atlj.database.business;
  exports esi.acgt.atlj.database.exceptions;

}