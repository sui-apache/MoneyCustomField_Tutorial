package com.example.plugins.tutorial.jira.customfields;

import com.atlassian.jira.issue.customfields.impl.AbstractSingleFieldType;
import com.atlassian.jira.issue.customfields.persistence.PersistenceFieldType;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import java.math.BigDecimal;


public class MoneyCustomField extends AbstractSingleFieldType<BigDecimal> {
    public MoneyCustomField(
            @JiraImport CustomFieldValuePersister customFieldValuePersister,
            @JiraImport GenericConfigManager genericConfigManager) {
        super(customFieldValuePersister, genericConfigManager);
    }


    public String getStringFromSingularObject(final BigDecimal singularObject) {
        if (singularObject == null)
            return null;
        else
            return singularObject.toString();
    }

    public BigDecimal getSingularObjectFromString(final String string) throws FieldValidationException {
        if (string == null)
            return null;
        try {
            BigDecimal decimal = new BigDecimal(string);
            if (decimal.scale() > 2) {
                throw new FieldValidationException(
                        "Maximum of 2 decimal places are allowed.");
            }
            return decimal.setScale(2);
        } catch (NumberFormatException ex) {
            throw new FieldValidationException("Not a valid number.");
        }
    }

    protected PersistenceFieldType getDatabaseType() {
        return PersistenceFieldType.TYPE_LIMITED_TEXT;
    }

    protected BigDecimal getObjectFromDbValue(final Object databaseValue)
            throws FieldValidationException {
        return getSingularObjectFromString((String) databaseValue);
    }

    protected Object getDbValueFromObject(final BigDecimal customFieldObject) {
        return getStringFromSingularObject(customFieldObject);
    }
}
