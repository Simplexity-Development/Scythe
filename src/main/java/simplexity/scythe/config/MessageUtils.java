package simplexity.scythe.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import simplexity.scythe.Scythe;

public class MessageUtils {

    private static final MiniMessage miniMessage = Scythe.getMiniMessage();

    public static TagResolver getTimeFormat(long timeSeconds) {
        long seconds = timeSeconds % 60;
        long minutes = (timeSeconds / 60) % 60;
        long hours = (timeSeconds / (60 * 60)) % 24;
        Component hourComponent = Component.empty();
        Component minuteComponent = Component.empty();
        Component secondComponent = Component.empty();
        if (hours > 0) {
            if (hours == 1) {
                hourComponent = parseNumber(Message.INSERT_TIME_FORMAT_HOUR.getMessage(), hours);
            } else {
                hourComponent = parseNumber(Message.INSERT_TIME_FORMAT_HOURS.getMessage(), hours);
            }
        }
        if (minutes > 0) {
            if (minutes == 1) {
                minuteComponent = parseNumber(Message.INSERT_TIME_FORMAT_MINUTE.getMessage(), minutes);
            } else {
                minuteComponent = parseNumber(Message.INSERT_TIME_FORMAT_MINUTES.getMessage(), minutes);
            }
        }
        if (seconds > 0) {
            if (seconds == 1) {
                secondComponent = parseNumber(Message.INSERT_TIME_FORMAT_SECOND.getMessage(), seconds);
            } else {
                secondComponent = parseNumber(Message.INSERT_TIME_FORMAT_SECONDS.getMessage(), seconds);
            }
        }
        Component finalComponent = miniMessage.deserialize(Message.INSERT_TIME_FORMAT_GROUP.getMessage(),
                Placeholder.component("hour", hourComponent),
                Placeholder.component("min", minuteComponent),
                Placeholder.component("sec", secondComponent));
        return TagResolver.resolver(Placeholder.component("time", finalComponent));
    }

    private static Component parseNumber(String message, long number) {
        return miniMessage.deserialize(message,
                Placeholder.parsed("count", String.valueOf(number)));
    }


}
