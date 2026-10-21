package pk.cinejoy.tv;

import android.net.Uri;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class AdBlocker {

    private static final Set<String> AD_DOMAINS = new HashSet<>(Arrays.asList(
            "llvpn.com",
            "highperformanceformat.com",
            "alwingulla.com",
            "a.wing.st",
            "popads.net",
            "popcash.net",
            "adsterra.com",
            "exoclick.com",
            "propellerads.com",
            "juicyads.com",
            "trafficjunky.com",
            "trafficjunky.net",
            "syndication.exoclick.com",
            "syndication.realsrv.com",
            "realsrv.com",
            "clickadu.com",
            "monetag.com",
            "adnxs.com",
            "doubleclick.net",
            "googlesyndication.com",
            "googleads.g.doubleclick.net",
            "pagead2.googlesyndication.com",
            "mgid.com",
            "taboola.com",
            "outbrain.com",
            "disqus.com",
            "bet",
            "1xbet"
    ));

    private static final String[] AD_KEYWORDS = {
            "/tag.min.js",
            "zone=",
            "popunder",
            "popup",
            "banner_ad",
            "sponsor",
            "adservice",
            "adsystem",
            "analytics",
            "telemetry"
    };

    public static boolean isAd(Uri uri) {
        if (uri == null) return false;
        String host = uri.getHost();
        String fullUrl = uri.toString().toLowerCase();

        if (host != null) {
            host = host.toLowerCase();
            for (String domain : AD_DOMAINS) {
                if (host.equals(domain) || host.endsWith("." + domain)) {
                    return true;
                }
            }
        }

        for (String kw : AD_KEYWORDS) {
            if (fullUrl.contains(kw)) {
                return true;
            }
        }

        return false;
    }

    public static ByteArrayInputStream createEmptyStream() {
        return new ByteArrayInputStream(new byte[0]);
    }

    public static final String JS_INJECTION =
            "(function() {\n" +
            "  try {\n" +
            "    localStorage.setItem('addies', 'false');\n" +
            "  } catch (e) {}\n" +
            "  \n" +
            "  // Neutralize popups and redirects\n" +
            "  try {\n" +
            "    window.open = function() {\n" +
            "      console.log('[AdBlock] Blocked popup window.open');\n" +
            "      return null;\n" +
            "    };\n" +
            "  } catch (e) {}\n" +
            "  \n" +
            "  // Inject CSS to remove ad placeholders and add TV focus glow\n" +
            "  function injectStyles() {\n" +
            "    var el = document.getElementById('cinejoy-tv-styles');\n" +
            "    if (!el && document.head) {\n" +
            "      el = document.createElement('style');\n" +
            "      el.id = 'cinejoy-tv-styles';\n" +
            "      el.textContent = `\n" +
            "        /* Block ad iframes and popups */\n" +
            "        iframe[src*=\"ad\"], iframe[src*=\"track\"], iframe[src*=\"syndication\"],\n" +
            "        div[class*=\"ad-\"], div[id*=\"ad-\"], [class*=\"popunder\"], [id*=\"popunder\"],\n" +
            "        div[class*=\"banner-ad\"], a[target=\"_blank\"][rel*=\"sponsored\"] {\n" +
            "          display: none !important;\n" +
            "          visibility: hidden !important;\n" +
            "          pointer-events: none !important;\n" +
            "          width: 0 !important;\n" +
            "          height: 0 !important;\n" +
            "        }\n" +
            "        /* TV D-Pad focus ring with Cinejoy Green */\n" +
            "        :focus {\n" +
            "          outline: 3px solid #95FF50 !important;\n" +
            "          outline-offset: 3px !important;\n" +
            "          box-shadow: 0 0 16px rgba(149, 255, 80, 0.6) !important;\n" +
            "          transition: outline 0.15s ease, box-shadow 0.15s ease !important;\n" +
            "        }\n" +
            "      `;\n" +
            "      document.head.appendChild(el);\n" +
            "    }\n" +
            "  }\n" +
            "  if (document.readyState === 'loading') {\n" +
            "    document.addEventListener('DOMContentLoaded', injectStyles);\n" +
            "  } else {\n" +
            "    injectStyles();\n" +
            "  }\n" +
            "})();";
}
