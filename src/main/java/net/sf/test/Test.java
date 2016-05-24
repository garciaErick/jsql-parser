package net.sf.test;

import java.io.StringReader;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class Test {
  public static void main(String[] args) throws JSQLParserException {
    CCJSqlParserManager pm = new CCJSqlParserManager();
//     String sql = "SELECT `dbowlizer` . `department` . `name` AS `name` , `dbowlizer` . `department` . `name` AS `name` , `dbowlizer` .  `department` . `location` AS `location` , `dbowlizer` . `city` .  `city_id` AS `city_id` , `dbowlizer` . `city` . `city_name` AS `city_name` , `dbowlizer` . `city` . `province` AS `province` FROM ( `dbowlizer` . `department` JOIN `dbowlizer` . `city` ON ( ( `dbowlizer` .  `department` . `location` = `dbowlizer` . `city` . `city_name`))) WHERE ( `dbowlizer` . `city` . `province` = 'ONTARIO')";
     String sql = "SELECT * FROM MY_TABLE1, MY_TABLE2, (SELECT * FROM MY_TABLE3) LEFT OUTER JOIN MY_TABLE4 WHERE ID = (SELECT MAX(ID) FROM MY_TABLE5) AND ID2 IN (SELECT * FROM MY_TABLE6)";
//     String sql = "SELECT col1 , col2 FROM TAB1 INNER JOIN TAB2 ON TAB1.a=TAB2.b WHERE a>5";
//     String sql = "SELECT a,b FROM Tab" ;
    Statement statement = pm.parse(new StringReader(sql));

    /**
     * now you should use a class that implements StatementVisitor to decide
     * what to do based on the kind of the statement, that is SELECT or INSERT
     * etc.
     */
    System.out.println("Query: " + sql + "\n");
    // Select Clause
    if (statement instanceof Select) {
      Select selectStatement = (Select) statement;
      TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
      List<String> tableList = tablesNamesFinder.getTableList(selectStatement);
      int i = 1;
      System.out.println("Table Names Clause:");
      for (String tableName : tableList) {
        System.out.println("  Table Name " + i + ": " + tableName);
        i++;
      }
      System.out.println();

      PlainSelect plainSelectStatement = (PlainSelect) selectStatement.getSelectBody();
      // From Item
      if (plainSelectStatement.getFromItem() != null) {
        System.out.println("From item clause:");
        FromItem fromItem = plainSelectStatement.getFromItem();
        System.out.println("  " + fromItem);
        System.out.println();
      }

      // Select Items
      if (plainSelectStatement.getSelectItems() != null) {
        System.out.println("Select items clause:");
        List<SelectItem> selectItems = plainSelectStatement.getSelectItems();
        i = 1;
        SelectExpressionItem selectExpression;
        for (SelectItem selectItem : selectItems) {
          System.out.println("  Select Item " + i + ": " + selectItem);
          try{
          selectExpression = (SelectExpressionItem) selectItem;
          // Column Alias
          if (selectExpression.getExpression() != null)
            System.out.println("    Select Expression" + i + ": " + selectExpression.getExpression());
          if (selectExpression.getAlias() != null)
            System.out.println("    Select Alias" + i + ": " + selectExpression.getAlias());
          System.out.println();
          } catch(Exception e){
            
          }
          i++;
        }
        System.out.println();
      }

      // Joins Clause
      if (plainSelectStatement.getJoins() != null) {
        List<Join> joinsList = plainSelectStatement.getJoins();
        i = 1;
        System.out.println("Joins Clause:");
        for (Join joinName : joinsList) {
          System.out.println("  Join Name " + i + ": " + joinName);
          i++;
        }
        System.out.println();
      }

      // Where Clause
      if (plainSelectStatement.getWhere() != null) {
        Expression whereClause = plainSelectStatement.getWhere();
        System.out.println("Where Clause:");
        System.out.println("  " + whereClause);
        System.out.println();
      }
    }
  }
}
