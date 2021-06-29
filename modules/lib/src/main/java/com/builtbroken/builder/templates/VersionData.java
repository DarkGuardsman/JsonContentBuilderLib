package com.builtbroken.builder.templates;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.data.IJsonGeneratedObject;
import com.builtbroken.builder.mapper.anno.JsonConstructor;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.builtbroken.builder.mapper.anno.JsonTemplate;

/**
 * Version information template
 * <p>
 * Uses @see <a href="https://www.semver.org/">SemVer</a>
 * <p>
 * Example: 3.5.6 or 3.4.6.2
 * <p>
 * Created by Robin Seifert on 2019-05-15.
 */
@JsonTemplate(ContentBuilderRefs.TYPE_VERSION_DATA)
public class VersionData extends AbstractLevelData implements IJsonGeneratedObject
{
    @JsonMapping(keys = "major", type = ConverterRefs.INT)
    protected Integer major;

    @JsonMapping(keys = "minor", type = ConverterRefs.INT)
    protected Integer minor;

    @JsonMapping(keys = "rev", type = ConverterRefs.INT)
    protected Integer rev;

    @JsonMapping(keys = "build", type = ConverterRefs.INT)
    protected Integer build;

    /**
     * Loads in the version as a single string for lazy developers
     *
     * @param version
     */
    @JsonMapping(keys = "version", type = ConverterRefs.STRING)
    public void loadVersion(String version)
    {
        if (!version.contains("."))
        {
            build = parseInt(version, version, "Build Number");
        }
        else
        {
            final String[] split = version.split("\\.");

            if(split.length < 2) {
                throw new IllegalArgumentException(String.format("Failed to parse version '%s'", version));
            }

            major = parseInt(split[0], version, "Major");
            minor = parseInt(split[1], version, "Minor");
            if (split.length > 2)
            {
                rev = parseInt(split[2], version, "Revision");
                if (split.length > 3) //TODO change to split on '+' optionally
                {
                    build = parseInt(split[3], version, "Build Number");
                    if (split.length > 4)
                    {
                        throw new IllegalArgumentException(String.format("Version can only contain 4 numbers split by '.', instead got %s values from '%s'", split.length, version));
                    }
                }
            }
        }
    }

    private int parseInt(final String value, final String version, final String section)
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            throw new NumberFormatException(String.format("Failed to parse version '%s' to get %s from '%s'", version, section, value));
        }
    }

    @JsonConstructor
    public static VersionData create(@JsonMapping(keys = "id", type = ConverterRefs.STRING, required = true) String name,
                                     @JsonMapping(keys = "level", type = ConverterRefs.ENUM) MetaDataLevel type)
    {
        VersionData data = new VersionData();
        data.id = name;
        data.level = type;
        if (data.level == null)
        {
            data.level = MetaDataLevel.OBJECT;
        }
        return data;
    }

    /**
     * Generates a string for the version
     *
     * @return version string, or "err" for invalid state
     */
    public String getVersion()
    {
        if (major != null || minor != null || rev != null)
        {
            if (minor != null || rev != null || build != null)
            {
                if (rev != null || build != null)
                {
                    if (build != null)
                    {
                        return String.format("%s.%s.%s.%s", major, minor, rev, build);
                    }
                    return String.format("%s.%s.%s", major, minor, rev);
                }
                return String.format("%s.%s", major, minor);
            }
            return String.format("%s", major);
        }
        else if (build != null)
        {
            return String.format("%s", build);
        }
        return "err";
    }

    @Override
    public boolean isValid()
    {
        return super.isValid()
                && !getVersion().equalsIgnoreCase("err")
                && !getVersion().contains("null");
    }

    @Override
    public String getJsonTemplateID()
    {
        return ContentBuilderRefs.TYPE_VERSION_DATA;
    }

    @Override
    public String toString()
    {
        return String.format("VersionData[%s, %s, %s]", getMetaDataLevel(), getJsonUniqueID(), getVersion());
    }
}
