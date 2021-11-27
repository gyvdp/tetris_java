module atlj.DB {
  requires java.sql;
  requires org.xerial.sqlitejdbc;

  exports esi.acgt.atlj.database.db;
  exports esi.acgt.atlj.database.dto;
  exports esi.acgt.atlj.database.buisness;

}