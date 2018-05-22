package team.stephen.sunshine.constant.solr;

/**
 * @author stephen
 */

public enum Field {
    ARTICLE_ID("articleId"),
    ARTICLE_CONTENT("articleContent"),
    ARTICLE_TAG("articleTag"),
    ARTICLE_PRIVATE("isPrivate"),
    ARTICLE_DELETED("deleted");
    private String fieldName;

    Field(String f) {
        setFieldName(f);
    }

    public static boolean containsField(String sort) {
        for (Field fn : Field.values()) {
            if (fn.getFieldName().endsWith(sort)) {
                return true;
            }
        }
        return false;
    }

    public java.lang.String getFieldName() {
        return fieldName;
    }

    public void setFieldName(java.lang.String fieldName) {
        this.fieldName = fieldName;
    }
}
