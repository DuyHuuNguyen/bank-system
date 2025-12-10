package com.bank.transaction_service.infrastructure.enums;

import lombok.Getter;

@Getter
public enum DateSqlTemplate {
  DATE_SQL_TEMPLATE("%s-%s");

  private String content;

  private DateSqlTemplate(String content) {
    this.content = content;
  }

  public static DateSqlTemplate getDateSqlTemplate(String year, String month) {

    return DateSqlTemplate.DATE_SQL_TEMPLATE.addContent(year, month);
  }

  private DateSqlTemplate addContent(String year, String month) {
    this.content = String.format(this.content, year, month);
    return this;
  }

  public static void main(String[] args) {
    DateSqlTemplate dateSqlTemplate = DateSqlTemplate.getDateSqlTemplate("2019", "09");
    System.out.println(dateSqlTemplate.getContent());
  }
}
