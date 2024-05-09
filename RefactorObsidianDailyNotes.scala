import java.io.File
import java.time.format.TextStyle
import java.time.{LocalDate, Month, Year}
import java.util.Locale
import os.Path

object RefactorObsidianDailyNotes:

  private val calendarPath: Path =
    os.home / "Library" / "Mobile Documents" / "iCloud~md~obsidian" / "Documents" / "KevsVault" / "0-Calendar"

  private val FileNameRegexp = raw"(\d{4})-(\d{2})-(\d{2}).md".r

  def run(): Unit =
    os.list(calendarPath)
      .filter(_.toIO.isFile)
      .foreach: path =>
        path.last match
          case FileNameRegexp(yyyy, mm, dd) =>
            val year      = Integer.parseInt(yyyy)
            val month     = Month.of(Integer.parseInt(mm))
            val monthName = month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
            val day       = Integer.parseInt(mm)
            val date      = LocalDate.of(year, month, day)
            val dayOfWeek = date.getDayOfWeek.getDisplayName(
              TextStyle.FULL,
              Locale.ENGLISH
            )
            val target = calendarPath / s"$yyyy" /
              s"$mm-$monthName" /
              (s"$yyyy-$mm-$dd-$dayOfWeek" + ".md")
            os.move(
              from = path,
              to = target,
              createFolders = true
            )
            println(
              s"Moved ${path.relativeTo(calendarPath)} " +
                s"to ${target.relativeTo(calendarPath)}"
            )
          case _ => ()
end RefactorObsidianDailyNotes
