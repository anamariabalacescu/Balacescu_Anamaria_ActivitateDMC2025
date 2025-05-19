package com.example.activitatedmc;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BancaDao_Impl implements BancaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Banca> __insertionAdapterOfBanca;

  private final EntityDeletionOrUpdateAdapter<Banca> __updateAdapterOfBanca;

  private final SharedSQLiteStatement __preparedStmtOfDeleteBanciWithFilialeGreaterThan;

  private final SharedSQLiteStatement __preparedStmtOfDeleteBanciWithFilialeLessThan;

  private final SharedSQLiteStatement __preparedStmtOfIncrementFilialeForBanciStartingWith;

  public BancaDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBanca = new EntityInsertionAdapter<Banca>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `banca_table` (`id`,`numeBanca`,`numarFiliale`,`tipBanca`,`rating`,`tipuriCredite`,`internetBanking`,`dataInfiintare`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Banca value) {
        stmt.bindLong(1, value.getId());
        if (value.getNumeBanca() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getNumeBanca());
        }
        stmt.bindLong(3, value.getNumarFiliale());
        final String _tmp = Converters.fromTipBanca(value.getTipBanca());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, _tmp);
        }
        stmt.bindDouble(5, value.getRating());
        final String _tmp_1 = Converters.fromTipCreditList(value.getTipuriCredite());
        if (_tmp_1 == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, _tmp_1);
        }
        final int _tmp_2 = value.hasInternetBanking() ? 1 : 0;
        stmt.bindLong(7, _tmp_2);
        final Long _tmp_3 = Converters.dateToTimestamp(value.getDataInfiintare());
        if (_tmp_3 == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindLong(8, _tmp_3);
        }
      }
    };
    this.__updateAdapterOfBanca = new EntityDeletionOrUpdateAdapter<Banca>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `banca_table` SET `id` = ?,`numeBanca` = ?,`numarFiliale` = ?,`tipBanca` = ?,`rating` = ?,`tipuriCredite` = ?,`internetBanking` = ?,`dataInfiintare` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Banca value) {
        stmt.bindLong(1, value.getId());
        if (value.getNumeBanca() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getNumeBanca());
        }
        stmt.bindLong(3, value.getNumarFiliale());
        final String _tmp = Converters.fromTipBanca(value.getTipBanca());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, _tmp);
        }
        stmt.bindDouble(5, value.getRating());
        final String _tmp_1 = Converters.fromTipCreditList(value.getTipuriCredite());
        if (_tmp_1 == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, _tmp_1);
        }
        final int _tmp_2 = value.hasInternetBanking() ? 1 : 0;
        stmt.bindLong(7, _tmp_2);
        final Long _tmp_3 = Converters.dateToTimestamp(value.getDataInfiintare());
        if (_tmp_3 == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindLong(8, _tmp_3);
        }
        stmt.bindLong(9, value.getId());
      }
    };
    this.__preparedStmtOfDeleteBanciWithFilialeGreaterThan = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM banca_table WHERE numarFiliale > ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteBanciWithFilialeLessThan = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM banca_table WHERE numarFiliale < ?";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementFilialeForBanciStartingWith = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE banca_table SET numarFiliale = numarFiliale + 1 WHERE numeBanca LIKE ? || '%'";
        return _query;
      }
    };
  }

  @Override
  public long insert(final Banca banca) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfBanca.insertAndReturnId(banca);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int update(final Banca banca) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__updateAdapterOfBanca.handle(banca);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int deleteBanciWithFilialeGreaterThan(final int value) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteBanciWithFilialeGreaterThan.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, value);
    __db.beginTransaction();
    try {
      final int _result = _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteBanciWithFilialeGreaterThan.release(_stmt);
    }
  }

  @Override
  public int deleteBanciWithFilialeLessThan(final int value) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteBanciWithFilialeLessThan.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, value);
    __db.beginTransaction();
    try {
      final int _result = _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteBanciWithFilialeLessThan.release(_stmt);
    }
  }

  @Override
  public int incrementFilialeForBanciStartingWith(final String letter) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementFilialeForBanciStartingWith.acquire();
    int _argIndex = 1;
    if (letter == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, letter);
    }
    __db.beginTransaction();
    try {
      final int _result = _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
      __preparedStmtOfIncrementFilialeForBanciStartingWith.release(_stmt);
    }
  }

  @Override
  public List<Banca> getAllBanci() {
    final String _sql = "SELECT * FROM banca_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfNumeBanca = CursorUtil.getColumnIndexOrThrow(_cursor, "numeBanca");
      final int _cursorIndexOfNumarFiliale = CursorUtil.getColumnIndexOrThrow(_cursor, "numarFiliale");
      final int _cursorIndexOfTipBanca = CursorUtil.getColumnIndexOrThrow(_cursor, "tipBanca");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfTipuriCredite = CursorUtil.getColumnIndexOrThrow(_cursor, "tipuriCredite");
      final int _cursorIndexOfInternetBanking = CursorUtil.getColumnIndexOrThrow(_cursor, "internetBanking");
      final int _cursorIndexOfDataInfiintare = CursorUtil.getColumnIndexOrThrow(_cursor, "dataInfiintare");
      final List<Banca> _result = new ArrayList<Banca>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Banca _item;
        _item = new Banca();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpNumeBanca;
        if (_cursor.isNull(_cursorIndexOfNumeBanca)) {
          _tmpNumeBanca = null;
        } else {
          _tmpNumeBanca = _cursor.getString(_cursorIndexOfNumeBanca);
        }
        _item.setNumeBanca(_tmpNumeBanca);
        final int _tmpNumarFiliale;
        _tmpNumarFiliale = _cursor.getInt(_cursorIndexOfNumarFiliale);
        _item.setNumarFiliale(_tmpNumarFiliale);
        final Banca.TipBanca _tmpTipBanca;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfTipBanca)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfTipBanca);
        }
        _tmpTipBanca = Converters.toTipBanca(_tmp);
        _item.setTipBanca(_tmpTipBanca);
        final double _tmpRating;
        _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
        _item.setRating(_tmpRating);
        final List<Banca.TipCredit> _tmpTipuriCredite;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfTipuriCredite)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfTipuriCredite);
        }
        _tmpTipuriCredite = Converters.toTipCreditList(_tmp_1);
        _item.setTipuriCredite(_tmpTipuriCredite);
        final boolean _tmpInternetBanking;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfInternetBanking);
        _tmpInternetBanking = _tmp_2 != 0;
        _item.setInternetBanking(_tmpInternetBanking);
        final Date _tmpDataInfiintare;
        final Long _tmp_3;
        if (_cursor.isNull(_cursorIndexOfDataInfiintare)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getLong(_cursorIndexOfDataInfiintare);
        }
        _tmpDataInfiintare = Converters.fromTimestamp(_tmp_3);
        _item.setDataInfiintare(_tmpDataInfiintare);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Banca getBancaByName(final String name) {
    final String _sql = "SELECT * FROM banca_table WHERE numeBanca = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfNumeBanca = CursorUtil.getColumnIndexOrThrow(_cursor, "numeBanca");
      final int _cursorIndexOfNumarFiliale = CursorUtil.getColumnIndexOrThrow(_cursor, "numarFiliale");
      final int _cursorIndexOfTipBanca = CursorUtil.getColumnIndexOrThrow(_cursor, "tipBanca");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfTipuriCredite = CursorUtil.getColumnIndexOrThrow(_cursor, "tipuriCredite");
      final int _cursorIndexOfInternetBanking = CursorUtil.getColumnIndexOrThrow(_cursor, "internetBanking");
      final int _cursorIndexOfDataInfiintare = CursorUtil.getColumnIndexOrThrow(_cursor, "dataInfiintare");
      final Banca _result;
      if(_cursor.moveToFirst()) {
        _result = new Banca();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpNumeBanca;
        if (_cursor.isNull(_cursorIndexOfNumeBanca)) {
          _tmpNumeBanca = null;
        } else {
          _tmpNumeBanca = _cursor.getString(_cursorIndexOfNumeBanca);
        }
        _result.setNumeBanca(_tmpNumeBanca);
        final int _tmpNumarFiliale;
        _tmpNumarFiliale = _cursor.getInt(_cursorIndexOfNumarFiliale);
        _result.setNumarFiliale(_tmpNumarFiliale);
        final Banca.TipBanca _tmpTipBanca;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfTipBanca)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfTipBanca);
        }
        _tmpTipBanca = Converters.toTipBanca(_tmp);
        _result.setTipBanca(_tmpTipBanca);
        final double _tmpRating;
        _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
        _result.setRating(_tmpRating);
        final List<Banca.TipCredit> _tmpTipuriCredite;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfTipuriCredite)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfTipuriCredite);
        }
        _tmpTipuriCredite = Converters.toTipCreditList(_tmp_1);
        _result.setTipuriCredite(_tmpTipuriCredite);
        final boolean _tmpInternetBanking;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfInternetBanking);
        _tmpInternetBanking = _tmp_2 != 0;
        _result.setInternetBanking(_tmpInternetBanking);
        final Date _tmpDataInfiintare;
        final Long _tmp_3;
        if (_cursor.isNull(_cursorIndexOfDataInfiintare)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getLong(_cursorIndexOfDataInfiintare);
        }
        _tmpDataInfiintare = Converters.fromTimestamp(_tmp_3);
        _result.setDataInfiintare(_tmpDataInfiintare);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Banca> getBanciBetweenFiliale(final int min, final int max) {
    final String _sql = "SELECT * FROM banca_table WHERE numarFiliale BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, min);
    _argIndex = 2;
    _statement.bindLong(_argIndex, max);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfNumeBanca = CursorUtil.getColumnIndexOrThrow(_cursor, "numeBanca");
      final int _cursorIndexOfNumarFiliale = CursorUtil.getColumnIndexOrThrow(_cursor, "numarFiliale");
      final int _cursorIndexOfTipBanca = CursorUtil.getColumnIndexOrThrow(_cursor, "tipBanca");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfTipuriCredite = CursorUtil.getColumnIndexOrThrow(_cursor, "tipuriCredite");
      final int _cursorIndexOfInternetBanking = CursorUtil.getColumnIndexOrThrow(_cursor, "internetBanking");
      final int _cursorIndexOfDataInfiintare = CursorUtil.getColumnIndexOrThrow(_cursor, "dataInfiintare");
      final List<Banca> _result = new ArrayList<Banca>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Banca _item;
        _item = new Banca();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpNumeBanca;
        if (_cursor.isNull(_cursorIndexOfNumeBanca)) {
          _tmpNumeBanca = null;
        } else {
          _tmpNumeBanca = _cursor.getString(_cursorIndexOfNumeBanca);
        }
        _item.setNumeBanca(_tmpNumeBanca);
        final int _tmpNumarFiliale;
        _tmpNumarFiliale = _cursor.getInt(_cursorIndexOfNumarFiliale);
        _item.setNumarFiliale(_tmpNumarFiliale);
        final Banca.TipBanca _tmpTipBanca;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfTipBanca)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfTipBanca);
        }
        _tmpTipBanca = Converters.toTipBanca(_tmp);
        _item.setTipBanca(_tmpTipBanca);
        final double _tmpRating;
        _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
        _item.setRating(_tmpRating);
        final List<Banca.TipCredit> _tmpTipuriCredite;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfTipuriCredite)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfTipuriCredite);
        }
        _tmpTipuriCredite = Converters.toTipCreditList(_tmp_1);
        _item.setTipuriCredite(_tmpTipuriCredite);
        final boolean _tmpInternetBanking;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfInternetBanking);
        _tmpInternetBanking = _tmp_2 != 0;
        _item.setInternetBanking(_tmpInternetBanking);
        final Date _tmpDataInfiintare;
        final Long _tmp_3;
        if (_cursor.isNull(_cursorIndexOfDataInfiintare)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getLong(_cursorIndexOfDataInfiintare);
        }
        _tmpDataInfiintare = Converters.fromTimestamp(_tmp_3);
        _item.setDataInfiintare(_tmpDataInfiintare);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
